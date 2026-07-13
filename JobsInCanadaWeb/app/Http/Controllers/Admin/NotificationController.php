<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\JobListing;
use App\Services\FCMService;
use Illuminate\Http\Request;
use Illuminate\View\View;

class NotificationController extends Controller
{
    public function index(): View
    {
        $jobs = JobListing::where('is_active', true)->orderBy('title')->get();
        return view('admin.notifications.index', compact('jobs'));
    }

    public function send(Request $request)
    {
        $request->validate([
            'title' => 'required|string|max:255',
            'body' => 'required|string',
            'image_url' => 'nullable|url',
            'screen' => 'required|string|in:home,detail',
            'job_id' => 'nullable|required_if:screen,detail|exists:job_listings,id',
        ]);

        try {
            FCMService::send(
                title: $request->title,
                body: $request->body,
                imageUrl: $request->image_url,
                screen: $request->screen,
                jobId: $request->job_id
            );

            return redirect()->route('admin.notifications.index')->with('success', 'Push notification broadcasted successfully!');
        } catch (\Exception $e) {
            return redirect()->back()->withInput()->with('error', 'Error: ' . $e->getMessage());
        }
    }

    public function getJobDetails(JobListing $job)
    {
        $job->load('company');
        
        $title = "New Job: " . $job->title;
        $companyName = $job->company?->name ?? 'Unknown Company';
        $body = "Apply now! " . $job->title . " position is open at " . $companyName . " in " . ($job->location ?: 'Canada') . ".";
        
        // Select a random image from notificationsimages folder
        $imageUrl = FCMService::getRandomNotificationImageUrl();

        return response()->json([
            'title' => $title,
            'body' => $body,
            'image_url' => $imageUrl ?? '',
            'job_id' => $job->id,
        ]);
    }

    public function sendRandom(Request $request)
    {
        $request->validate([
            'type' => 'required|in:job',
        ]);

        try {
            // Pick a random active job listing (prioritizing today's jobs, then falling back to any active job)
            $job = JobListing::with('company')
                ->where('is_active', true)
                ->whereDate('posted_at', today())
                ->inRandomOrder()
                ->first();

            if (!$job) {
                $job = JobListing::with('company')
                    ->where('is_active', true)
                    ->inRandomOrder()
                    ->first();
            }

            if (!$job) {
                return response()->json(['success' => false, 'message' => 'No active jobs found in the database.'], 404);
            }

            $title = "New Job: " . $job->title;
            $companyName = $job->company?->name ?? 'Unknown Company';
            $body = "Apply now! " . $job->title . " position is open at " . $companyName . " in " . ($job->location ?: 'Canada') . ".";
            $imageUrl = FCMService::getRandomNotificationImageUrl();

            FCMService::send(
                title: $title,
                body: $body,
                imageUrl: $imageUrl,
                screen: 'detail',
                jobId: $job->id
            );

            return response()->json(['success' => true, 'message' => "Random job notification sent: '{$title}'"]);

        } catch (\Exception $e) {
            return response()->json(['success' => false, 'message' => 'Failed to send notification: ' . $e->getMessage()], 500);
        }
    }
}

<?php

namespace App\Console\Commands;

use App\Models\JobListing;
use App\Services\FCMService;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\Log;

class SendRandomDramaNotification extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'app:send-random-drama-notification';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Send a random push notification for a job listing';

    /**
     * Execute the console command.
     */
    public function handle()
    {
        try {
            // Pick a random active job listing posted today
            $job = JobListing::with('company')
                ->where('is_active', true)
                ->whereDate('posted_at', today())
                ->inRandomOrder()
                ->first();

            // Fallback: Pick any random active job listing if none were posted today
            if (!$job) {
                $job = JobListing::with('company')
                    ->where('is_active', true)
                    ->inRandomOrder()
                    ->first();
            }

            if (!$job) {
                $this->error('No active jobs found in database.');
                Log::warning('Command SendRandomDramaNotification: No active jobs found.');
                return 1;
            }

            $title = "New Job: " . $job->title;
            $companyName = $job->company?->name ?? 'Unknown Company';
            $body = "Apply now! " . $job->title . " position is open at " . $companyName . " in " . ($job->location ?: 'Canada') . ".";
            $imageUrl = FCMService::getRandomNotificationImageUrl();

            $this->info("Selected Job ID {$job->id} ('{$job->title}') to send. Sending...");

            FCMService::send(
                title: $title,
                body: $body,
                imageUrl: $imageUrl,
                screen: 'detail',
                jobId: $job->id
            );

            $this->info("Successfully sent job notification: '{$title}'");
            Log::info("Command SendRandomDramaNotification: Sent notification for job ID {$job->id} successfully.");
            return 0;
            
        } catch (\Exception $e) {
            $this->error("Failed to send notification. Error: " . $e->getMessage());
            Log::error("Command SendRandomDramaNotification error: " . $e->getMessage());
            return 1;
        }
    }
}

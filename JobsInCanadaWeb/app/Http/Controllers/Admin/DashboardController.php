<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\CareerResource;
use App\Models\Category;
use App\Models\Company;
use App\Models\JobListing;
use App\Models\SiteSetting;
use Illuminate\View\View;

class DashboardController extends Controller
{
    public function index(): View
    {
        $stats = [
            'jobs' => JobListing::count(),
            'active_jobs' => JobListing::where('is_active', true)->count(),
            'featured_jobs' => JobListing::where('is_featured', true)->count(),
            'companies' => Company::count(),
            'categories' => Category::count(),
            'resources' => CareerResource::count(),
            'jobs_today' => (int) SiteSetting::get('jobs_today', 0),
            'jobs_this_week' => (int) SiteSetting::get('jobs_this_week', 0),
        ];

        $recentJobs = JobListing::with(['company', 'category'])
            ->latest()
            ->limit(5)
            ->get();

        return view('admin.dashboard', compact('stats', 'recentJobs'));
    }
}

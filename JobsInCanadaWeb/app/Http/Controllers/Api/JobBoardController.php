<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\CareerResource;
use App\Models\Category;
use App\Models\Company;
use App\Models\JobListing;
use App\Models\Province;
use App\Models\SiteSetting;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class JobBoardController extends Controller
{
    public function categories(): JsonResponse
    {
        $categories = Category::orderBy('sort_order')
            ->orderBy('name')
            ->withCount('jobListings')
            ->get()
            ->map(fn ($c) => [
                'id' => $c->id,
                'label' => $c->name,
                'slug' => $c->slug,
                'icon' => $c->icon,
                'color' => $c->color,
                'iconColor' => $c->icon_color ?? $c->color,
                'count' => $c->job_listings_count,
            ]);

        return response()->json($categories);
    }

    public function provinces(): JsonResponse
    {
        $provinces = Province::orderBy('sort_order')
            ->orderBy('name')
            ->get(['id', 'name', 'code']);

        return response()->json($provinces);
    }

    public function companies(): JsonResponse
    {
        $companies = Company::orderBy('sort_order')
            ->orderBy('name')
            ->withCount('jobListings')
            ->get()
            ->map(fn ($c) => [
                'id' => $c->id,
                'name' => $c->name,
                'logoUrl' => $c->logo,
                'semanticLabel' => $c->name.' company logo',
                'jobCount' => $c->job_listings_count,
                'website' => $c->website,
                'description' => $c->description,
            ]);

        return response()->json($companies);
    }

    public function jobs(Request $request): JsonResponse
    {
        $query = JobListing::with(['company', 'category'])->where('is_active', true);

        if ($request->filled('q')) {
            $query->where(function ($q) use ($request) {
                $q->where('title', 'like', '%'.$request->q.'%')
                  ->orWhereHas('company', fn ($c) => $c->where('name', 'like', '%'.$request->q.'%'));
            });
        }

        if ($request->filled('category')) {
            $query->whereHas('category', fn ($q) => $q->where('slug', $request->category)
                ->orWhere('name', $request->category));
        }

        if ($request->boolean('featured')) {
            $query->where('is_featured', true);
        }

        if ($request->boolean('remote')) {
            $query->where('is_remote', true);
        }

        if ($request->filled('type')) {
            $query->where('job_type', $request->type);
        }

        if ($request->filled('province')) {
            $query->where('province', $request->province);
        }

        if ($request->boolean('new')) {
            $query->where('is_new', true);
        }

        if ($request->boolean('today')) {
            $query->whereDate('posted_at', today());
        }

        if ($request->filled('min_salary')) {
            $query->where('salary_min', '>=', (int) $request->min_salary);
        }

        $perPage = min((int) $request->get('per_page', 20), 100);
        $jobs = $query->latest('posted_at')->latest()->paginate($perPage);

        $jobs->getCollection()->transform(fn ($job) => $this->jobToMap($job));

        return response()->json($jobs);
    }

    public function job(JobListing $job): JsonResponse
    {
        if (! $job->is_active) {
            return response()->json(['message' => 'Job not found.'], 404);
        }

        return response()->json($this->jobToMap($job));
    }

    public function careerResources(): JsonResponse
    {
        $resources = CareerResource::orderBy('sort_order')
            ->orderBy('title')
            ->get()
            ->map(fn ($r) => [
                'id' => $r->id,
                'icon' => $r->icon,
                'title' => $r->title,
                'subtitle' => $r->subtitle,
                'color' => $r->color,
                'iconColor' => $r->icon_color,
            ]);

        return response()->json($resources);
    }

    public function stats(): JsonResponse
    {
        return response()->json([
            'jobs' => JobListing::where('is_active', true)->count(),
            'companies' => Company::count(),
            'categories' => Category::count(),
            'featured' => JobListing::where('is_active', true)->where('is_featured', true)->count(),
        ]);
    }

    public function settings(): JsonResponse
    {
        return response()->json([
            'jobsToday' => (int) SiteSetting::get('jobs_today', 0),
            'jobsThisWeek' => (int) SiteSetting::get('jobs_this_week', 0),
        ]);
    }

    protected function jobToMap(JobListing $job): array
    {
        return [
            'id' => $job->id,
            'title' => $job->title,
            'company' => $job->company?->name,
            'companyLogo' => $job->logoUrl(),
            'companyLogoSemanticLabel' => $job->logoLabel(),
            'salary' => $job->salary,
            'salaryPeriod' => $job->salary_period,
            'salaryMin' => $job->salary_min,
            'location' => $job->location,
            'jobType' => $job->job_type,
            'isRemote' => (bool) $job->is_remote,
            'isNew' => (bool) $job->is_new,
            'applicants' => (int) $job->applicants,
            'category' => $job->category?->name,
            'province' => $job->province,
            'postedDaysAgo' => $job->postedDaysAgo(),
            'isFeatured' => (bool) $job->is_featured,
            'isSaved' => false,
            'applicantAvatars' => $job->applicant_avatars ?? [],
            'applyUrl' => $job->apply_url,
            'description' => $job->description,
            'skills' => $job->skills ?? [],
        ];
    }
}

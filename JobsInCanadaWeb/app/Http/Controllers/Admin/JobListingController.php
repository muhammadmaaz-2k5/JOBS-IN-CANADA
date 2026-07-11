<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Category;
use App\Models\Company;
use App\Models\JobListing;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class JobListingController extends Controller
{
    public function index(Request $request): View
    {
        $query = JobListing::with(['company', 'category']);

        if ($request->filled('q')) {
            $query->where(function ($q) use ($request) {
                $q->where('title', 'like', '%'.$request->q.'%')
                  ->orWhereHas('company', fn ($c) => $c->where('name', 'like', '%'.$request->q.'%'));
            });
        }

        if ($request->filled('category')) {
            $query->where('category_id', $request->category);
        }

        $jobs = $query->latest()->paginate(15)->withQueryString();
        $categories = Category::orderBy('name')->get();

        return view('admin.jobs.index', compact('jobs', 'categories'));
    }

    public function create(): View
    {
        $companies = Company::orderBy('name')->get();
        $categories = Category::orderBy('name')->get();

        return view('admin.jobs.form', [
            'job' => null,
            'companies' => $companies,
            'categories' => $categories,
        ]);
    }

    public function store(Request $request): RedirectResponse
    {
        $data = $this->validateJob($request);

        JobListing::create($data);

        return redirect()->route('admin.jobs.index')
            ->with('success', 'Job created successfully.');
    }

    protected function prepareSkills(?string $value): ?array
    {
        if ($value === null || trim($value) === '') {
            return null;
        }

        return array_values(array_filter(array_map('trim', explode(',', $value))));
    }

    protected function prepareAvatars(?string $value): ?array
    {
        if ($value === null || trim($value) === '') {
            return null;
        }

        return array_values(array_filter(array_map('trim', preg_split('/\R|,/', $value) ?: [])));
    }

    public function edit(JobListing $job): View
    {
        $companies = Company::orderBy('name')->get();
        $categories = Category::orderBy('name')->get();

        return view('admin.jobs.form', compact('job', 'companies', 'categories'));
    }

    public function update(Request $request, JobListing $job): RedirectResponse
    {
        $data = $this->validateJob($request, $job);

        $job->update($data);

        return redirect()->route('admin.jobs.index')
            ->with('success', 'Job updated successfully.');
    }

    public function destroy(JobListing $job): RedirectResponse
    {
        $job->delete();

        return redirect()->route('admin.jobs.index')
            ->with('success', 'Job deleted successfully.');
    }

    protected function validateJob(Request $request, ?JobListing $job = null): array
    {
        $data = $request->validate([
            'title' => ['required', 'string', 'max:255'],
            'slug' => ['nullable', 'string', 'max:255', 'unique:job_listings,slug,'.($job?->id ?? 'NULL')],
            'company_id' => ['nullable', 'exists:companies,id'],
            'category_id' => ['nullable', 'exists:categories,id'],
            'company_logo' => ['nullable', 'url', 'max:1024'],
            'company_logo_label' => ['nullable', 'string', 'max:255'],
            'salary' => ['nullable', 'string', 'max:60'],
            'salary_period' => ['nullable', 'string', 'max:20'],
            'location' => ['nullable', 'string', 'max:255'],
            'province' => ['nullable', 'string', 'max:255'],
            'job_type' => ['nullable', 'string', 'max:60'],
            'is_remote' => ['boolean'],
            'is_new' => ['boolean'],
            'is_featured' => ['boolean'],
            'is_active' => ['boolean'],
            'applicants' => ['nullable', 'integer', 'min:0'],
            'apply_url' => ['nullable', 'url', 'max:1024'],
            'description' => ['nullable', 'string'],
            'skills' => ['nullable', 'string'],
            'applicant_avatars' => ['nullable', 'string'],
            'posted_at' => ['nullable', 'date'],
        ]);

        $data['skills'] = $this->prepareSkills($request->input('skills'));
        $data['applicant_avatars'] = $this->prepareAvatars($request->input('applicant_avatars'));

        return $data;
    }
}

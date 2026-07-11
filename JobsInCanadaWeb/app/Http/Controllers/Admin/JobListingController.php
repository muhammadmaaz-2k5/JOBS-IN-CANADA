<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Category;
use App\Models\Company;
use App\Models\JobListing;
use App\Models\Province;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class JobListingController extends Controller
{
    public function index(Request $request): View
    {
        $featuredQuery = JobListing::with(['company', 'category'])->where('is_featured', true);
        $regularQuery = JobListing::with(['company', 'category'])->where('is_featured', false);

        if ($request->filled('q')) {
            $search = function ($q) use ($request) {
                $q->where('title', 'like', '%'.$request->q.'%')
                  ->orWhereHas('company', fn ($c) => $c->where('name', 'like', '%'.$request->q.'%'));
            };
            $featuredQuery->where($search);
            $regularQuery->where($search);
        }

        if ($request->filled('category')) {
            $featuredQuery->where('category_id', $request->category);
            $regularQuery->where('category_id', $request->category);
        }

        $featuredJobs = $featuredQuery->latest()->get();
        $jobs = $regularQuery->latest()->paginate(15)->withQueryString();
        $categories = Category::orderBy('name')->get();

        return view('admin.jobs.index', compact('featuredJobs', 'jobs', 'categories'));
    }

    public function create(): View
    {
        $companies = Company::orderBy('name')->get();
        $categories = Category::orderBy('name')->get();
        $provinces = Province::orderBy('sort_order')->orderBy('name')->get();

        return view('admin.jobs.form', [
            'job' => null,
            'companies' => $companies,
            'categories' => $categories,
            'provinces' => $provinces,
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
        $provinces = Province::orderBy('sort_order')->orderBy('name')->get();

        return view('admin.jobs.form', compact('job', 'companies', 'categories', 'provinces'));
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
            'salary_min' => ['nullable', 'integer', 'min:0'],
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

        $data['is_remote'] = $request->boolean('is_remote');
        $data['is_new'] = $request->boolean('is_new');
        $data['is_featured'] = $request->boolean('is_featured');
        $data['is_active'] = $request->boolean('is_active');

        if (empty($data['posted_at'])) {
            $data['posted_at'] = now();
        }

        return $data;
    }

    public function importJson(Request $request): RedirectResponse
    {
        $jsonData = $request->input('json_data');

        if (empty($jsonData)) {
            return back()->with('error', 'Please provide JSON data.');
        }

        $json = json_decode($jsonData, true);

        if (json_last_error() !== JSON_ERROR_NONE) {
            return back()->with('error', 'Invalid JSON structure: ' . json_last_error_msg());
        }

        // If it's a single job (associative array with a title key directly)
        $jobsData = isset($json['title']) ? [$json] : $json;

        if (!is_array($jobsData) || empty($jobsData)) {
            return back()->with('error', 'JSON must be a job object or a non-empty array of job objects.');
        }

        $importedCount = 0;
        $errors = [];

        foreach ($jobsData as $index => $item) {
            if (!is_array($item)) {
                $errors[] = "Job #" . ($index + 1) . " is not a valid object.";
                continue;
            }

            if (!isset($item['title']) || trim($item['title']) === '') {
                $errors[] = "Job #" . ($index + 1) . " is missing a title.";
                continue;
            }

            // Find or create Company by name
            $companyId = null;
            if (isset($item['company']) && trim($item['company']) !== '') {
                $company = Company::firstOrCreate([
                    'name' => trim($item['company'])
                ], [
                    'slug' => \Illuminate\Support\Str::slug($item['company']),
                ]);
                $companyId = $company->id;
            } elseif (isset($item['company_id'])) {
                $companyId = $item['company_id'];
            }

            // Find or create Category by name
            $categoryId = null;
            if (isset($item['category']) && trim($item['category']) !== '') {
                $category = Category::firstOrCreate([
                    'name' => trim($item['category'])
                ], [
                    'slug' => \Illuminate\Support\Str::slug($item['category']),
                ]);
                $categoryId = $category->id;
            } elseif (isset($item['category_id'])) {
                $categoryId = $item['category_id'];
            }

            // Process skills
            $skills = null;
            if (isset($item['skills'])) {
                if (is_array($item['skills'])) {
                    $skills = array_values(array_filter(array_map('trim', $item['skills'])));
                } else {
                    $skills = $this->prepareSkills($item['skills']);
                }
            }

            // Process applicant avatars
            $avatars = null;
            if (isset($item['applicant_avatars'])) {
                if (is_array($item['applicant_avatars'])) {
                    $avatars = array_values(array_filter(array_map('trim', $item['applicant_avatars'])));
                } else {
                    $avatars = $this->prepareAvatars($item['applicant_avatars']);
                }
            }

            JobListing::create([
                'title' => $item['title'],
                'slug' => $item['slug'] ?? \Illuminate\Support\Str::slug($item['title'] . '-' . uniqid()),
                'company_id' => $companyId,
                'category_id' => $categoryId,
                'company_logo' => $item['company_logo'] ?? $item['companyLogo'] ?? null,
                'company_logo_label' => $item['company_logo_label'] ?? $item['companyLogoSemanticLabel'] ?? null,
                'salary' => $item['salary'] ?? null,
                'salary_period' => $item['salary_period'] ?? 'year',
                'salary_min' => $item['salary_min'] ?? $item['salaryMin'] ?? 0,
                'location' => $item['location'] ?? null,
                'province' => $item['province'] ?? null,
                'job_type' => $item['job_type'] ?? $item['jobType'] ?? null,
                'is_remote' => filter_var($item['is_remote'] ?? $item['isRemote'] ?? false, FILTER_VALIDATE_BOOLEAN),
                'is_new' => filter_var($item['is_new'] ?? $item['isNew'] ?? true, FILTER_VALIDATE_BOOLEAN),
                'is_featured' => filter_var($item['is_featured'] ?? $item['isFeatured'] ?? false, FILTER_VALIDATE_BOOLEAN),
                'is_active' => filter_var($item['is_active'] ?? $item['isActive'] ?? true, FILTER_VALIDATE_BOOLEAN),
                'applicants' => $item['applicants'] ?? 0,
                'apply_url' => $item['apply_url'] ?? $item['applyUrl'] ?? null,
                'description' => $item['description'] ?? null,
                'skills' => $skills,
                'applicant_avatars' => $avatars,
                'posted_at' => isset($item['posted_at']) ? \Carbon\Carbon::parse($item['posted_at']) : now(),
            ]);

            $importedCount++;
        }

        if (count($errors) > 0) {
            return redirect()->route('admin.jobs.index')
                ->with('success', "Imported {$importedCount} jobs. Errors: " . implode(' ', $errors));
        }

        return redirect()->route('admin.jobs.index')
            ->with('success', "Imported {$importedCount} jobs successfully from JSON.");
    }
}

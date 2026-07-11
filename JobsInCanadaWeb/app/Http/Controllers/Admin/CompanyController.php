<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Company;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class CompanyController extends Controller
{
    public function index(): View
    {
        $companies = Company::withCount('jobListings')
            ->orderBy('sort_order')
            ->orderBy('name')
            ->get();

        return view('admin.companies.index', compact('companies'));
    }

    public function create(): View
    {
        return view('admin.companies.form', ['company' => null]);
    }

    public function store(Request $request): RedirectResponse
    {
        $data = $request->validate([
            'name' => ['required', 'string', 'max:255'],
            'slug' => ['nullable', 'string', 'max:255', 'unique:companies,slug'],
            'logo' => ['nullable', 'url', 'max:1024'],
            'website' => ['nullable', 'url', 'max:1024'],
            'description' => ['nullable', 'string'],
            'sort_order' => ['nullable', 'integer'],
        ]);

        Company::create($data);

        return redirect()->route('admin.companies.index')
            ->with('success', 'Company created successfully.');
    }

    public function edit(Company $company): View
    {
        return view('admin.companies.form', compact('company'));
    }

    public function update(Request $request, Company $company): RedirectResponse
    {
        $data = $request->validate([
            'name' => ['required', 'string', 'max:255'],
            'slug' => ['nullable', 'string', 'max:255', 'unique:companies,slug,'.$company->id],
            'logo' => ['nullable', 'url', 'max:1024'],
            'website' => ['nullable', 'url', 'max:1024'],
            'description' => ['nullable', 'string'],
            'sort_order' => ['nullable', 'integer'],
        ]);

        $company->update($data);

        return redirect()->route('admin.companies.index')
            ->with('success', 'Company updated successfully.');
    }

    public function destroy(Company $company): RedirectResponse
    {
        $company->delete();

        return redirect()->route('admin.companies.index')
            ->with('success', 'Company deleted successfully.');
    }
}

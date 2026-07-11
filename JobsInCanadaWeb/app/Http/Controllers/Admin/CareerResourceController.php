<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\CareerResource;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class CareerResourceController extends Controller
{
    public function index(): View
    {
        $resources = CareerResource::orderBy('sort_order')->orderBy('title')->get();

        return view('admin.career-resources.index', compact('resources'));
    }

    public function create(): View
    {
        return view('admin.career-resources.form', ['resource' => null]);
    }

    public function store(Request $request): RedirectResponse
    {
        $data = $request->validate([
            'title' => ['required', 'string', 'max:255'],
            'slug' => ['nullable', 'string', 'max:255', 'unique:career_resources,slug'],
            'subtitle' => ['nullable', 'string', 'max:255'],
            'icon' => ['nullable', 'string', 'max:120'],
            'color' => ['nullable', 'string', 'max:9'],
            'icon_color' => ['nullable', 'string', 'max:9'],
            'sort_order' => ['nullable', 'integer'],
        ]);

        CareerResource::create($data);

        return redirect()->route('admin.career-resources.index')
            ->with('success', 'Career resource created successfully.');
    }

    public function edit(CareerResource $resource): View
    {
        return view('admin.career-resources.form', compact('resource'));
    }

    public function update(Request $request, CareerResource $resource): RedirectResponse
    {
        $data = $request->validate([
            'title' => ['required', 'string', 'max:255'],
            'slug' => ['nullable', 'string', 'max:255', 'unique:career_resources,slug,'.$resource->id],
            'subtitle' => ['nullable', 'string', 'max:255'],
            'icon' => ['nullable', 'string', 'max:120'],
            'color' => ['nullable', 'string', 'max:9'],
            'icon_color' => ['nullable', 'string', 'max:9'],
            'sort_order' => ['nullable', 'integer'],
        ]);

        $resource->update($data);

        return redirect()->route('admin.career-resources.index')
            ->with('success', 'Career resource updated successfully.');
    }

    public function destroy(CareerResource $resource): RedirectResponse
    {
        $resource->delete();

        return redirect()->route('admin.career-resources.index')
            ->with('success', 'Career resource deleted successfully.');
    }
}

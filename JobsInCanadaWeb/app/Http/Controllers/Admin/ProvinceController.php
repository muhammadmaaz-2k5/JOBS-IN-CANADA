<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Province;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class ProvinceController extends Controller
{
    public function index(): View
    {
        $provinces = Province::orderBy('sort_order')->orderBy('name')->get();

        return view('admin.provinces.index', compact('provinces'));
    }

    public function create(): View
    {
        return view('admin.provinces.form', ['province' => null]);
    }

    public function store(Request $request): RedirectResponse
    {
        $data = $request->validate([
            'name' => ['required', 'string', 'max:255'],
            'code' => ['required', 'string', 'max:4'],
            'sort_order' => ['nullable', 'integer'],
        ]);

        Province::create($data);

        return redirect()->route('admin.provinces.index')
            ->with('success', 'Province created successfully.');
    }

    public function edit(Province $province): View
    {
        return view('admin.provinces.form', compact('province'));
    }

    public function update(Request $request, Province $province): RedirectResponse
    {
        $data = $request->validate([
            'name' => ['required', 'string', 'max:255'],
            'code' => ['required', 'string', 'max:4'],
            'sort_order' => ['nullable', 'integer'],
        ]);

        $province->update($data);

        return redirect()->route('admin.provinces.index')
            ->with('success', 'Province updated successfully.');
    }

    public function destroy(Province $province): RedirectResponse
    {
        $province->delete();

        return redirect()->route('admin.provinces.index')
            ->with('success', 'Province deleted successfully.');
    }
}

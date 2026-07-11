<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Logo;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class LogoController extends Controller
{
    public function index(): View
    {
        $logos = Logo::orderBy('sort_order')->orderBy('name')->get();

        return view('admin.logos.index', compact('logos'));
    }

    public function create(): View
    {
        return view('admin.logos.form', ['logo' => null]);
    }

    public function store(Request $request): RedirectResponse
    {
        $data = $request->validate([
            'name' => ['required', 'string', 'max:255'],
            'url' => ['required', 'url', 'max:1024'],
            'sort_order' => ['nullable', 'integer'],
        ]);

        Logo::create($data);

        return redirect()->route('admin.logos.index')
            ->with('success', 'Logo added to library.');
    }

    public function edit(Logo $logo): View
    {
        return view('admin.logos.form', compact('logo'));
    }

    public function update(Request $request, Logo $logo): RedirectResponse
    {
        $data = $request->validate([
            'name' => ['required', 'string', 'max:255'],
            'url' => ['required', 'url', 'max:1024'],
            'sort_order' => ['nullable', 'integer'],
        ]);

        $logo->update($data);

        return redirect()->route('admin.logos.index')
            ->with('success', 'Logo updated.');
    }

    public function destroy(Logo $logo): RedirectResponse
    {
        $logo->delete();

        return redirect()->route('admin.logos.index')
            ->with('success', 'Logo removed from library.');
    }
}

<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\SiteSetting;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class SettingsController extends Controller
{
    public function index(): View
    {
        return view('admin.settings', [
            'jobsToday' => (int) SiteSetting::get('jobs_today', 0),
            'jobsThisWeek' => (int) SiteSetting::get('jobs_this_week', 0),
        ]);
    }

    public function update(Request $request): RedirectResponse
    {
        $data = $request->validate([
            'jobs_today' => ['required', 'integer', 'min:0'],
            'jobs_this_week' => ['required', 'integer', 'min:0'],
        ]);

        SiteSetting::set('jobs_today', $data['jobs_today']);
        SiteSetting::set('jobs_this_week', $data['jobs_this_week']);

        return redirect()->route('admin.settings.index')
            ->with('success', 'Settings updated successfully.');
    }
}

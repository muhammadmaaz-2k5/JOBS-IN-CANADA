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
            'adsEnabled' => SiteSetting::get('ads_enabled', 'false'),
            'enableWebviewAds' => SiteSetting::get('enable_webview_ads', 'false'),
            'webviewAdUrl' => SiteSetting::get('webview_ad_url', 'https://nazaarabox.com'),
            'appMode' => SiteSetting::get('app_mode', 'live'),
        ]);
    }

    public function update(Request $request): RedirectResponse
    {
        $data = $request->validate([
            'jobs_today' => ['required', 'integer', 'min:0'],
            'jobs_this_week' => ['required', 'integer', 'min:0'],
            'ads_enabled' => ['required', 'in:true,false'],
            'enable_webview_ads' => ['required', 'in:true,false'],
            'webview_ad_url' => ['required', 'url'],
            'app_mode' => ['required', 'in:live,safe_review'],
        ]);

        SiteSetting::set('jobs_today', $data['jobs_today']);
        SiteSetting::set('jobs_this_week', $data['jobs_this_week']);
        SiteSetting::set('ads_enabled', $data['ads_enabled']);
        SiteSetting::set('enable_webview_ads', $data['enable_webview_ads']);
        SiteSetting::set('webview_ad_url', $data['webview_ad_url']);
        SiteSetting::set('app_mode', $data['app_mode']);

        return redirect()->route('admin.settings.index')
            ->with('success', 'Settings updated successfully.');
    }
}

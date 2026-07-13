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
        $enableAdDetail = [];
        $adUrlDetail = [];
        for ($i = 1; $i <= 10; $i++) {
            $enableAdDetail[$i] = SiteSetting::get("enable_ad_detail_$i", "true");
            $adUrlDetail[$i] = SiteSetting::get("ad_url_detail_$i", "");
        }

        return view('admin.settings', [
            'jobsToday' => (int) SiteSetting::get('jobs_today', 0),
            'jobsThisWeek' => (int) SiteSetting::get('jobs_this_week', 0),
            'adsEnabled' => SiteSetting::get('ads_enabled', 'false'),
            'enableWebviewAds' => SiteSetting::get('enable_webview_ads', 'false'),
            'webviewAdUrl' => SiteSetting::get('webview_ad_url', 'https://nazaarabox.com'),
            'enableAdDetail' => $enableAdDetail,
            'adUrlDetail' => $adUrlDetail,
        ]);
    }

    public function update(Request $request): RedirectResponse
    {
        $validationRules = [
            'jobs_today' => ['required', 'integer', 'min:0'],
            'jobs_this_week' => ['required', 'integer', 'min:0'],
            'ads_enabled' => ['required', 'in:true,false'],
            'enable_webview_ads' => ['required', 'in:true,false'],
            'webview_ad_url' => ['required', 'url'],
        ];

        for ($i = 1; $i <= 10; $i++) {
            $validationRules["enable_ad_detail_$i"] = ['required', 'in:true,false'];
            $validationRules["ad_url_detail_$i"] = ['nullable', 'url'];
        }

        $data = $request->validate($validationRules);

        SiteSetting::set('jobs_today', $data['jobs_today']);
        SiteSetting::set('jobs_this_week', $data['jobs_this_week']);
        SiteSetting::set('ads_enabled', $data['ads_enabled']);
        SiteSetting::set('enable_webview_ads', $data['enable_webview_ads']);
        SiteSetting::set('webview_ad_url', $data['webview_ad_url']);

        for ($i = 1; $i <= 10; $i++) {
            SiteSetting::set("enable_ad_detail_$i", $data["enable_ad_detail_$i"]);
            SiteSetting::set("ad_url_detail_$i", $data["ad_url_detail_$i"] ?? '');
        }

        return redirect()->route('admin.settings.index')
            ->with('success', 'Settings updated successfully.');
    }
}

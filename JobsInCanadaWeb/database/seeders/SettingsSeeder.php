<?php

namespace Database\Seeders;

use App\Models\SiteSetting;
use Illuminate\Database\Seeder;

class SettingsSeeder extends Seeder
{
    public function run(): void
    {
        SiteSetting::set('jobs_today', 124);
        SiteSetting::set('jobs_this_week', 847);
        SiteSetting::set('ads_enabled', 'false');
        SiteSetting::set('enable_webview_ads', 'false');
        SiteSetting::set('webview_ad_url', 'https://nazaarabox.com');
        for ($i = 1; $i <= 10; $i++) {
            SiteSetting::set("enable_ad_detail_$i", "true");
            SiteSetting::set("ad_url_detail_$i", "");
        }
    }
}

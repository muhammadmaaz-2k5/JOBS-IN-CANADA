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
    }
}

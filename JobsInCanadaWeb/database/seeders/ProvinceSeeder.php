<?php

namespace Database\Seeders;

use App\Models\Province;
use Illuminate\Database\Seeder;

class ProvinceSeeder extends Seeder
{
    public function run(): void
    {
        $provinces = [
            ['name' => 'Alberta', 'code' => 'AB', 'sort_order' => 1],
            ['name' => 'British Columbia', 'code' => 'BC', 'sort_order' => 2],
            ['name' => 'Manitoba', 'code' => 'MB', 'sort_order' => 3],
            ['name' => 'New Brunswick', 'code' => 'NB', 'sort_order' => 4],
            ['name' => 'Newfoundland and Labrador', 'code' => 'NL', 'sort_order' => 5],
            ['name' => 'Nova Scotia', 'code' => 'NS', 'sort_order' => 6],
            ['name' => 'Ontario', 'code' => 'ON', 'sort_order' => 7],
            ['name' => 'Prince Edward Island', 'code' => 'PE', 'sort_order' => 8],
            ['name' => 'Quebec', 'code' => 'QC', 'sort_order' => 9],
            ['name' => 'Saskatchewan', 'code' => 'SK', 'sort_order' => 10],
            ['name' => 'Northwest Territories', 'code' => 'NT', 'sort_order' => 11],
            ['name' => 'Nunavut', 'code' => 'NU', 'sort_order' => 12],
            ['name' => 'Yukon', 'code' => 'YT', 'sort_order' => 13],
        ];

        foreach ($provinces as $p) {
            Province::updateOrCreate(['name' => $p['name']], $p);
        }
    }
}

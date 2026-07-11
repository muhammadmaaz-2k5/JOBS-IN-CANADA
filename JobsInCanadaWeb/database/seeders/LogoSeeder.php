<?php

namespace Database\Seeders;

use App\Models\Logo;
use Illuminate\Database\Seeder;

class LogoSeeder extends Seeder
{
    public function run(): void
    {
        $logos = [
            ['name' => 'Shopify', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1fd7282dc-1772974660577.png', 'sort_order' => 1],
            ['name' => 'Wealthsimple', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1ed3b6573-1783736379632.png', 'sort_order' => 2],
            ['name' => 'Figma', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_14898759e-1772037482288.png', 'sort_order' => 3],
            ['name' => 'Microsoft', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_174ad6426-1783736379527.png', 'sort_order' => 4],
            ['name' => 'Airbnb', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_19ed1076f-1783736379938.png', 'sort_order' => 5],
            ['name' => 'Spotify', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1da815be6-1769423589184.png', 'sort_order' => 6],
            ['name' => 'Apple', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_19ec829ac-1783736379750.png', 'sort_order' => 7],
            ['name' => 'Slack', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_18b173690-1768637023216.png', 'sort_order' => 8],
            ['name' => 'Notion', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1caac3b51-1772623965291.png', 'sort_order' => 9],
            ['name' => 'RBC', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1cfdd4e14-1783736379911.png', 'sort_order' => 10],
            ['name' => 'TD Bank', 'url' => 'https://images.pexels.com/photos/259249/pexels-photo-259249.jpeg?w=80&h=80&fit=crop', 'sort_order' => 11],
            ['name' => 'Bombardier', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1070c7b03-1783736381221.png', 'sort_order' => 12],
            ['name' => 'Lululemon', 'url' => 'https://images.pexels.com/photos/1536619/pexels-photo-1536619.jpeg?w=80&h=80&fit=crop', 'sort_order' => 13],
            ['name' => 'Telus', 'url' => 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=80&h=80&fit=crop', 'sort_order' => 14],
            ['name' => 'Hootsuite', 'url' => 'https://images.pexels.com/photos/1181671/pexels-photo-1181671.jpeg?w=80&h=80&fit=crop', 'sort_order' => 15],
            ['name' => 'Manulife', 'url' => 'https://images.pixabay.com/photo/2016/11/27/21/42/stock-1863880_960_720.jpg', 'sort_order' => 16],
            ['name' => 'CAMH', 'url' => 'https://images.pexels.com/photos/4386466/pexels-photo-4386466.jpeg?w=80&h=80&fit=crop', 'sort_order' => 17],
            ['name' => 'Intuit', 'url' => 'https://img.rocket.new/generatedImages/rocket_gen_img_19938e190-1783736380383.png', 'sort_order' => 18],
        ];

        foreach ($logos as $l) {
            Logo::updateOrCreate(['url' => $l['url']], $l);
        }
    }
}

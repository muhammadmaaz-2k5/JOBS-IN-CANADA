<?php

namespace Database\Seeders;

use App\Models\Logo;
use Illuminate\Database\Seeder;

class LogoSeeder extends Seeder
{
    public function run(): void
    {
        $logos = [
            ['name' => 'Shopify', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/e/e1/Shopify_Logo.png', 'sort_order' => 1],
            ['name' => 'Wealthsimple', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Wealthsimple_logo.svg/512px-Wealthsimple_logo.svg.png', 'sort_order' => 2],
            ['name' => 'Figma', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Figma-logo.svg/400px-Figma-logo.svg.png', 'sort_order' => 3],
            ['name' => 'Microsoft', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/512px-Microsoft_logo.svg.png', 'sort_order' => 4],
            ['name' => 'Airbnb', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Airbnb_Logo_B%C3%A9lo.svg/512px-Airbnb_Logo_B%C3%A9lo.svg.png', 'sort_order' => 5],
            ['name' => 'Spotify', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Spotify_logo_without_text.svg/512px-Spotify_logo_without_text.svg.png', 'sort_order' => 6],
            ['name' => 'Apple', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/512px-Apple_logo_black.svg.png', 'sort_order' => 7],
            ['name' => 'Slack', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/Slack_icon_2019.svg/512px-Slack_icon_2019.svg.png', 'sort_order' => 8],
            ['name' => 'Notion', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/e/e9/Notion-logo.png', 'sort_order' => 9],
            ['name' => 'RBC', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Royal_Bank_of_Canada_Logo.svg/512px-Royal_Bank_of_Canada_Logo.svg.png', 'sort_order' => 10],
            ['name' => 'TD Bank', 'url' => 'https://images.pexels.com/photos/259249/pexels-photo-259249.jpeg?w=80&h=80&fit=crop', 'sort_order' => 11],
            ['name' => 'Bombardier', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/18/Bombardier_Logo.svg/512px-Bombardier_Logo.svg.png', 'sort_order' => 12],
            ['name' => 'Lululemon', 'url' => 'https://images.pexels.com/photos/1536619/pexels-photo-1536619.jpeg?w=80&h=80&fit=crop', 'sort_order' => 13],
            ['name' => 'Telus', 'url' => 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=80&h=80&fit=crop', 'sort_order' => 14],
            ['name' => 'Hootsuite', 'url' => 'https://images.pexels.com/photos/1181671/pexels-photo-1181671.jpeg?w=80&h=80&fit=crop', 'sort_order' => 15],
            ['name' => 'Manulife', 'url' => 'https://images.pixabay.com/photo/2016/11/27/21/42/stock-1863880_960_720.jpg', 'sort_order' => 16],
            ['name' => 'CAMH', 'url' => 'https://images.pexels.com/photos/4386466/pexels-photo-4386466.jpeg?w=80&h=80&fit=crop', 'sort_order' => 17],
            ['name' => 'Intuit', 'url' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Intuit_Logo.svg/512px-Intuit_Logo.svg.png', 'sort_order' => 18],
        ];

        foreach ($logos as $l) {
            Logo::updateOrCreate(['url' => $l['url']], $l);
        }
    }
}

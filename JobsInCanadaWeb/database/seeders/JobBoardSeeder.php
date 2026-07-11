<?php

namespace Database\Seeders;

use App\Models\CareerResource;
use App\Models\Category;
use App\Models\Company;
use App\Models\JobListing;
use Illuminate\Database\Seeder;

class JobBoardSeeder extends Seeder
{
    public function run(): void
    {
        $categories = [
            ['name' => 'Design', 'icon' => 'design_services_outlined', 'color' => '#F3E8FF', 'sort_order' => 1],
            ['name' => 'Marketing', 'icon' => 'campaign_outlined', 'color' => '#FEF3C7', 'sort_order' => 2],
            ['name' => 'Engineering', 'icon' => 'code_rounded', 'color' => '#DBEAFE', 'sort_order' => 3],
            ['name' => 'Product', 'icon' => 'inventory_2_outlined', 'color' => '#DCFCE7', 'sort_order' => 4],
            ['name' => 'Data', 'icon' => 'bar_chart_rounded', 'color' => '#FFEDD5', 'sort_order' => 5],
            ['name' => 'Finance', 'icon' => 'account_balance_outlined', 'color' => '#F0FDF4', 'sort_order' => 6],
            ['name' => 'Healthcare', 'icon' => 'health_and_safety_outlined', 'color' => '#FFF1F2', 'sort_order' => 7],
            ['name' => 'Legal', 'icon' => 'gavel_rounded', 'color' => '#F8FAFC', 'sort_order' => 8],
            ['name' => 'Sales', 'icon' => 'trending_up_rounded', 'color' => '#EFF6FF', 'sort_order' => 9],
            ['name' => 'Education', 'icon' => 'school_outlined', 'color' => '#FFF7ED', 'sort_order' => 10],
        ];
        foreach ($categories as $c) {
            Category::updateOrCreate(['name' => $c['name']], $c);
        }

        $companies = [
            ['name' => 'Shopify', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1fd7282dc-1772974660577.png', 'website' => 'https://www.shopify.com', 'sort_order' => 1],
            ['name' => 'Wealthsimple', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1ed3b6573-1783736379632.png', 'website' => 'https://www.wealthsimple.com', 'sort_order' => 2],
            ['name' => 'Figma', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_14898759e-1772037482288.png', 'website' => 'https://www.figma.com', 'sort_order' => 3],
            ['name' => 'Microsoft', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_174ad6426-1783736379527.png', 'website' => 'https://careers.microsoft.com', 'sort_order' => 4],
            ['name' => 'Airbnb', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_19ed1076f-1783736379938.png', 'website' => 'https://careers.airbnb.com', 'sort_order' => 5],
            ['name' => 'Spotify', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1da815be6-1769423589184.png', 'website' => 'https://www.spotify.com', 'sort_order' => 6],
            ['name' => 'Apple', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_19ec829ac-1783736379750.png', 'website' => 'https://www.apple.com', 'sort_order' => 7],
            ['name' => 'Slack', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_18b173690-1768637023216.png', 'website' => 'https://slack.com', 'sort_order' => 8],
            ['name' => 'Notion', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1caac3b51-1772623965291.png', 'website' => 'https://www.notion.so', 'sort_order' => 9],
            ['name' => 'RBC', 'logo' => 'https://img.rocket.new/generatedImages/rocket_gen_img_1cfdd4e14-1783736379911.png', 'website' => 'https://www.rbc.com', 'sort_order' => 10],
        ];
        foreach ($companies as $c) {
            Company::updateOrCreate(['name' => $c['name']], $c);
        }

        $resources = [
            ['title' => 'Resume Review', 'subtitle' => 'Get expert feedback on your Canadian resume', 'icon' => 'description_outlined', 'color' => '#DBEAFE', 'icon_color' => '#2563EB', 'sort_order' => 1],
            ['title' => 'Interview Tips', 'subtitle' => 'Ace your next Canadian job interview', 'icon' => 'chat_bubble_outline_rounded', 'color' => '#DCFCE7', 'icon_color' => '#16A34A', 'sort_order' => 2],
            ['title' => 'Salary Insights', 'subtitle' => 'Know your worth in the Canadian market', 'icon' => 'insights_rounded', 'color' => '#FEF3C7', 'icon_color' => '#D97706', 'sort_order' => 3],
            ['title' => 'Work Permit Guide', 'subtitle' => 'Understand LMIA & work authorization in Canada', 'icon' => 'school_outlined', 'color' => '#F3E8FF', 'icon_color' => '#A855F7', 'sort_order' => 4],
        ];
        foreach ($resources as $r) {
            CareerResource::updateOrCreate(['title' => $r['title']], $r);
        }

        if (JobListing::count() > 0) {
            return;
        }

        $design = Category::where('name', 'Design')->first();
        $eng = Category::where('name', 'Engineering')->first();
        $marketing = Category::where('name', 'Marketing')->first();

        $jobs = [
            [
                'title' => 'Senior Product Designer', 'company' => 'Shopify', 'category' => $design,
                'salary' => '$115K', 'salary_period' => 'year', 'location' => 'Ottawa, ON', 'province' => 'Ontario',
                'job_type' => 'Full-Time', 'is_remote' => false, 'is_new' => true, 'is_featured' => true, 'applicants' => 47,
                'apply_url' => 'https://www.shopify.com/careers',
                'description' => 'We are looking for an exceptional Senior Product Designer to join Shopify. You will own end-to-end design for merchant-facing products used by millions of businesses across Canada and beyond.',
                'skills' => ['Figma', 'Design Systems', 'User Research', 'Prototyping'],
                'posted_at' => now(),
            ],
            [
                'title' => 'Staff Software Engineer', 'company' => 'Wealthsimple', 'category' => $eng,
                'salary' => '$175K', 'salary_period' => 'year', 'location' => 'Toronto, ON', 'province' => 'Ontario',
                'job_type' => 'Full-Time', 'is_remote' => true, 'is_new' => true, 'is_featured' => true, 'applicants' => 83,
                'apply_url' => 'https://jobs.lever.co/wealthsimple',
                'description' => 'Join Wealthsimple as a Staff Software Engineer and help build the technology behind effortless financial services for Canadians.',
                'skills' => ['PHP', 'Laravel', 'React', 'AWS', 'PostgreSQL'],
                'posted_at' => now(),
            ],
            [
                'title' => 'UI Designer', 'company' => 'Figma', 'category' => $design,
                'salary' => '$110K', 'salary_period' => 'year', 'location' => 'Remote', 'province' => 'Remote',
                'job_type' => 'Full-Time', 'is_remote' => true, 'is_new' => false, 'is_featured' => false, 'applicants' => 29,
                'apply_url' => 'https://www.figma.com/careers',
                'description' => 'Craft delightful, intuitive interfaces that empower people to design together in real time.',
                'skills' => ['Figma', 'Visual Design', 'Interaction Design'],
                'posted_at' => now()->subDays(1),
            ],
            [
                'title' => 'UX Researcher', 'company' => 'Microsoft', 'category' => $design,
                'salary' => '$115K', 'salary_period' => 'year', 'location' => 'Vancouver, BC', 'province' => 'British Columbia',
                'job_type' => 'Full-Time', 'is_remote' => false, 'is_new' => true, 'is_featured' => false, 'applicants' => 14,
                'apply_url' => 'https://careers.microsoft.com',
                'description' => 'Drive user understanding across Microsoft Canada products through rigorous, mixed-method research.',
                'skills' => ['User Interviews', 'Usability Testing', 'Quant Research'],
                'posted_at' => now(),
            ],
            [
                'title' => 'Product Designer', 'company' => 'Airbnb', 'category' => $design,
                'salary' => '$105K', 'salary_period' => 'year', 'location' => 'Toronto, ON', 'province' => 'Ontario',
                'job_type' => 'Contract', 'is_remote' => false, 'is_new' => false, 'is_featured' => false, 'applicants' => 36,
                'apply_url' => 'https://careers.airbnb.com',
                'description' => 'Design magical travel experiences for a global community, from booking to belonging.',
                'skills' => ['Product Design', 'Figma', 'Design Systems'],
                'posted_at' => now()->subDays(2),
            ],
            [
                'title' => 'Growth Marketing Manager', 'company' => 'Spotify', 'category' => $marketing,
                'salary' => '$98K', 'salary_period' => 'year', 'location' => 'Toronto, ON', 'province' => 'Ontario',
                'job_type' => 'Full-Time', 'is_remote' => false, 'is_new' => true, 'is_featured' => false, 'applicants' => 22,
                'apply_url' => 'https://www.spotify.com/careers',
                'description' => 'Lead growth campaigns that connect Canadian listeners and creators with the music they love.',
                'skills' => ['Performance Marketing', 'Analytics', 'SEO', 'Paid Social'],
                'posted_at' => now()->subDays(1),
            ],
        ];

        foreach ($jobs as $j) {
            $company = Company::where('name', $j['company'])->first();
            JobListing::create([
                'title' => $j['title'],
                'company_id' => $company?->id,
                'category_id' => $j['category']?->id,
                'salary' => $j['salary'],
                'salary_period' => $j['salary_period'],
                'salary_min' => (int) preg_replace('/[^0-9]/', '', $j['salary']) * 1000,
                'location' => $j['location'],
                'province' => $j['province'],
                'job_type' => $j['job_type'],
                'is_remote' => $j['is_remote'],
                'is_new' => $j['is_new'],
                'is_featured' => $j['is_featured'],
                'applicants' => $j['applicants'],
                'apply_url' => $j['apply_url'],
                'description' => $j['description'],
                'skills' => $j['skills'],
                'posted_at' => $j['posted_at'],
                'is_active' => true,
            ]);
        }
    }
}

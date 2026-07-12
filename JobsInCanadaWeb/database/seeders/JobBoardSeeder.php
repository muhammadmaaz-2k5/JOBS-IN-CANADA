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
            ['name' => 'Shopify', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/e/e1/Shopify_Logo.png', 'website' => 'https://www.shopify.com', 'sort_order' => 1],
            ['name' => 'Wealthsimple', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Wealthsimple_logo.svg/512px-Wealthsimple_logo.svg.png', 'website' => 'https://www.wealthsimple.com', 'sort_order' => 2],
            ['name' => 'Figma', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Figma-logo.svg/400px-Figma-logo.svg.png', 'website' => 'https://www.figma.com', 'sort_order' => 3],
            ['name' => 'Microsoft', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/512px-Microsoft_logo.svg.png', 'website' => 'https://careers.microsoft.com', 'sort_order' => 4],
            ['name' => 'Airbnb', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Airbnb_Logo_B%C3%A9lo.svg/512px-Airbnb_Logo_B%C3%A9lo.svg.png', 'website' => 'https://careers.airbnb.com', 'sort_order' => 5],
            ['name' => 'Spotify', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Spotify_logo_without_text.svg/512px-Spotify_logo_without_text.svg.png', 'website' => 'https://www.spotify.com', 'sort_order' => 6],
            ['name' => 'Apple', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/512px-Apple_logo_black.svg.png', 'website' => 'https://www.apple.com', 'sort_order' => 7],
            ['name' => 'Slack', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/Slack_icon_2019.svg/512px-Slack_icon_2019.svg.png', 'website' => 'https://slack.com', 'sort_order' => 8],
            ['name' => 'Notion', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/e/e9/Notion-logo.png', 'website' => 'https://www.notion.so', 'sort_order' => 9],
            ['name' => 'RBC', 'logo' => 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Royal_Bank_of_Canada_Logo.svg/512px-Royal_Bank_of_Canada_Logo.svg.png', 'website' => 'https://www.rbc.com', 'sort_order' => 10],
        ];
        foreach ($companies as $c) {
            Company::firstOrCreate(['name' => $c['name']], $c);
        }

        $resources = [
            [
                'title' => 'Resume Review',
                'subtitle' => 'Get expert feedback on your Canadian resume',
                'icon' => 'description_outlined',
                'color' => '#DBEAFE',
                'icon_color' => '#2563EB',
                'sort_order' => 1,
                'content' => "# Writing a Canadian-Style Resume\n\nYour resume is the first impression you make on Canadian employers. Here is how to format it for success:\n\n## Format and Layout\n- **Reverse Chronological**: List your most recent experience first.\n- **One to Two Pages**: Keep it concise. Senior roles can be two pages.\n- **No Personal Details**: Never include your photo, date of birth, gender, marital status, or SIN.\n- **Contact Details**: Name, email, phone number, and city/province of residence (e.g. Toronto, ON). Include your LinkedIn profile.\n\n## Best Practices\n- **Action Verbs**: Start each bullet point with a strong action verb (e.g. Led, Developed, Managed).\n- **Quantified Results**: Show metrics. E.g. \"Increased sales by 15%\" instead of \"Responsible for sales\".\n- **Targeted Keywords**: Adapt your resume to match the job description keywords."
            ],
            [
                'title' => 'Interview Tips',
                'subtitle' => 'Ace your next Canadian job interview',
                'icon' => 'chat_bubble_outline_rounded',
                'color' => '#DCFCE7',
                'icon_color' => '#16A34A',
                'sort_order' => 2,
                'content' => "# Acing Your Canadian Job Interview\n\nCanadian employers value soft skills, cultural fit, and structured technical responses.\n\n## Preparation Steps\n- **Research the Company**: Learn about their values, products, and recent news.\n- **Master the STAR Method**:\n    - **S**ituation: Set the scene.\n    - **T**ask: What needed to be done.\n    - **A**ction: What you did.\n    - **R**esult: What was achieved (quantified).\n- **Practice Active Listening**: Take a moment to formulate your thoughts before replying.\n\n## Key Cultural Aspects\n- **Punctuality**: Arrive/Join 5 minutes early.\n- **Professionalism**: Dress appropriately, even for virtual interviews.\n- **Follow Up**: Send a brief thank-you email within 24 hours."
            ],
            [
                'title' => 'Salary Insights',
                'subtitle' => 'Know your worth in the Canadian market',
                'icon' => 'insights_rounded',
                'color' => '#FEF3C7',
                'icon_color' => '#D97706',
                'sort_order' => 3,
                'content' => "# Salary Insights & Negotiation\n\nUnderstanding compensation structures is key to securing a fair offer in Canada.\n\n## Market Research\n- **Research Averages**: Use platforms like Glassdoor, Indeed, and the Government of Canada Job Bank to check average rates for your role.\n- **Local Variations**: Salaries in major hubs like Toronto and Vancouver are higher, but cost of living is also higher.\n\n## Understanding Your Package\n- **Gross vs Net Salary**: Understand your take-home pay after provincial and federal income taxes.\n- **Benefits**: Look at health insurance, dental coverage, paid time off, and RRSP matching contributions.\n- **Negotiation**: Always let the employer make the first offer, then counter politely based on market data."
            ],
            [
                'title' => 'Work Permit Guide',
                'subtitle' => 'Understand LMIA & work authorization in Canada',
                'icon' => 'school_outlined',
                'color' => '#F3E8FF',
                'icon_color' => '#A855F7',
                'sort_order' => 4,
                'content' => "# Work Permit & Visa Guide\n\nTo work in Canada legally, you must obtain the correct work authorization.\n\n## Main Work Permit Types\n- **Open Work Permits**: Allows you to work for any employer (e.g., PGWP for graduates, Spousal Work Permits).\n- **Employer-Specific Work Permits**: Tied to a single employer, usually requiring a Labour Market Impact Assessment (LMIA).\n\n## Popular Pathways\n- **Express Entry**: The Federal Skilled Worker program for skilled immigrants.\n- **Provincial Nominee Programs (PNP)**: Run by individual provinces to fill specific labour needs.\n- **Post-Graduation Work Permit (PGWP)**: For international students who graduated from a Canadian DLI."
            ],
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

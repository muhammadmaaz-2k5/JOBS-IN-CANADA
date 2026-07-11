package com.job2day.jobsincanada.data

object MockData {
    val categories = listOf(
        Category(1, "Design", "design_services_outlined", "#F3E8FF", 3),
        Category(2, "Marketing", "campaign_outlined", "#FEF3C7", 1),
        Category(3, "Engineering", "code_rounded", "#DBEAFE", 2),
        Category(4, "Product", "inventory_2_outlined", "#DCFCE7", 0),
        Category(5, "Data", "bar_chart_rounded", "#FFEDD5", 0),
        Category(6, "Finance", "account_balance_outlined", "#F0FDF4", 0),
        Category(7, "Healthcare", "health_and_safety_outlined", "#FFF1F2", 0),
        Category(8, "Legal", "gavel_rounded", "#F8FAFC", 0),
        Category(9, "Sales", "trending_up_rounded", "#EFF6FF", 0),
        Category(10, "Education", "school_outlined", "#FFF7ED", 0)
    )

    val companies = listOf(
        Company(1, "Shopify", "https://img.rocket.new/generatedImages/rocket_gen_img_1fd7282dc-1772974660577.png", "https://www.shopify.com"),
        Company(2, "Wealthsimple", "https://img.rocket.new/generatedImages/rocket_gen_img_1ed3b6573-1783736379632.png", "https://www.wealthsimple.com"),
        Company(3, "Figma", "https://img.rocket.new/generatedImages/rocket_gen_img_14898759e-1772037482288.png", "https://www.figma.com"),
        Company(4, "Microsoft", "https://img.rocket.new/generatedImages/rocket_gen_img_174ad6426-1783736379527.png", "https://careers.microsoft.com"),
        Company(5, "Airbnb", "https://img.rocket.new/generatedImages/rocket_gen_img_19ed1076f-1783736379938.png", "https://careers.airbnb.com"),
        Company(6, "Spotify", "https://img.rocket.new/generatedImages/rocket_gen_img_1da815be6-1769423589184.png", "https://www.spotify.com"),
        Company(7, "Apple", "https://img.rocket.new/generatedImages/rocket_gen_img_19ec829ac-1783736379750.png", "https://www.apple.com"),
        Company(8, "Slack", "https://img.rocket.new/generatedImages/rocket_gen_img_18b173690-1768637023216.png", "https://slack.com"),
        Company(9, "Notion", "https://img.rocket.new/generatedImages/rocket_gen_img_1caac3b51-1772623965291.png", "https://www.notion.so"),
        Company(10, "RBC", "https://img.rocket.new/generatedImages/rocket_gen_img_1cfdd4e14-1783736379911.png", "https://www.rbc.com")
    )

    val careerResources = listOf(
        CareerResource(1, "Resume Review", "Get expert feedback on your Canadian resume", "description_outlined", "#DBEAFE", "#2563EB", "https://www.shopify.com/careers"),
        CareerResource(2, "Interview Tips", "Ace your next Canadian job interview", "chat_bubble_outline_rounded", "#DCFCE7", "#16A34A", "https://jobs.lever.co/wealthsimple"),
        CareerResource(3, "Salary Insights", "Know your worth in the Canadian market", "insights_rounded", "#FEF3C7", "#D97706", "https://www.figma.com/careers"),
        CareerResource(4, "Work Permit Guide", "Understand LMIA & work authorization in Canada", "school_outlined", "#F3E8FF", "#A855F7", "https://careers.microsoft.com")
    )

    val jobs = listOf(
        JobListing(
            id = 1,
            title = "Senior Product Designer",
            company = "Shopify",
            companyLogo = "https://img.rocket.new/generatedImages/rocket_gen_img_1fd7282dc-1772974660577.png",
            category = "Design",
            salary = "$115K",
            salaryPeriod = "year",
            salaryMin = 115000,
            location = "Ottawa, ON",
            province = "Ontario",
            jobType = "Full-Time",
            isRemote = false,
            isNew = true,
            isFeatured = true,
            applicants = 47,
            applyUrl = "https://www.shopify.com/careers",
            description = "We are looking for an exceptional Senior Product Designer to join Shopify. You will own end-to-end design for merchant-facing products used by millions of businesses across Canada and beyond.",
            skills = listOf("Figma", "Design Systems", "User Research", "Prototyping"),
            postedDaysAgo = 0,
            applicantAvatars = listOf(
                "https://randomuser.me/api/portraits/men/32.jpg",
                "https://randomuser.me/api/portraits/women/44.jpg",
                "https://randomuser.me/api/portraits/men/12.jpg"
            )
        ),
        JobListing(
            id = 2,
            title = "Staff Software Engineer",
            company = "Wealthsimple",
            companyLogo = "https://img.rocket.new/generatedImages/rocket_gen_img_1ed3b6573-1783736379632.png",
            category = "Engineering",
            salary = "$175K",
            salaryPeriod = "year",
            salaryMin = 175000,
            location = "Toronto, ON",
            province = "Ontario",
            jobType = "Full-Time",
            isRemote = true,
            isNew = true,
            isFeatured = true,
            applicants = 83,
            applyUrl = "https://jobs.lever.co/wealthsimple",
            description = "Join Wealthsimple as a Staff Software Engineer and help build the technology behind effortless financial services for Canadians. You'll drive core technical decisions, optimize APIs, and collaborate closely with product management.",
            skills = listOf("PHP", "Laravel", "React", "AWS", "PostgreSQL"),
            postedDaysAgo = 0,
            applicantAvatars = listOf(
                "https://randomuser.me/api/portraits/women/12.jpg",
                "https://randomuser.me/api/portraits/men/43.jpg",
                "https://randomuser.me/api/portraits/women/28.jpg"
            )
        ),
        JobListing(
            id = 3,
            title = "UI Designer",
            company = "Figma",
            companyLogo = "https://img.rocket.new/generatedImages/rocket_gen_img_14898759e-1772037482288.png",
            category = "Design",
            salary = "$110K",
            salaryPeriod = "year",
            salaryMin = 110000,
            location = "Remote",
            province = "Remote",
            jobType = "Full-Time",
            isRemote = true,
            isNew = false,
            isFeatured = false,
            applicants = 29,
            applyUrl = "https://www.figma.com/careers",
            description = "Craft delightful, intuitive interfaces that empower people to design together in real time. Focus on core visual styles, layout dynamics, and prototyping features.",
            skills = listOf("Figma", "Visual Design", "Interaction Design"),
            postedDaysAgo = 1,
            applicantAvatars = listOf(
                "https://randomuser.me/api/portraits/men/1.jpg",
                "https://randomuser.me/api/portraits/women/2.jpg",
                "https://randomuser.me/api/portraits/men/3.jpg"
            )
        ),
        JobListing(
            id = 4,
            title = "UX Researcher",
            company = "Microsoft",
            companyLogo = "https://img.rocket.new/generatedImages/rocket_gen_img_174ad6426-1783736379527.png",
            category = "Design",
            salary = "$115K",
            salaryPeriod = "year",
            salaryMin = 115000,
            location = "Vancouver, BC",
            province = "British Columbia",
            jobType = "Full-Time",
            isRemote = false,
            isNew = true,
            isFeatured = false,
            applicants = 14,
            applyUrl = "https://careers.microsoft.com",
            description = "Drive user understanding across Microsoft Canada products through rigorous, mixed-method research. Partner with designers and engineering teams to identify product opportunities.",
            skills = listOf("User Interviews", "Usability Testing", "Quant Research"),
            postedDaysAgo = 0,
            applicantAvatars = listOf(
                "https://randomuser.me/api/portraits/women/10.jpg",
                "https://randomuser.me/api/portraits/men/11.jpg"
            )
        ),
        JobListing(
            id = 5,
            title = "Product Designer",
            company = "Airbnb",
            companyLogo = "https://img.rocket.new/generatedImages/rocket_gen_img_19ed1076f-1783736379938.png",
            category = "Design",
            salary = "$105K",
            salaryPeriod = "year",
            salaryMin = 105000,
            location = "Toronto, ON",
            province = "Ontario",
            jobType = "Contract",
            isRemote = false,
            isNew = false,
            isFeatured = false,
            applicants = 36,
            applyUrl = "https://careers.airbnb.com",
            description = "Design magical travel experiences for a global community, from booking to belonging. We value storytelling, detail-oriented design execution, and high collaborative standards.",
            skills = listOf("Product Design", "Figma", "Design Systems"),
            postedDaysAgo = 2,
            applicantAvatars = listOf(
                "https://randomuser.me/api/portraits/men/50.jpg",
                "https://randomuser.me/api/portraits/women/51.jpg"
            )
        ),
        JobListing(
            id = 6,
            title = "Growth Marketing Manager",
            company = "Spotify",
            companyLogo = "https://img.rocket.new/generatedImages/rocket_gen_img_1da815be6-1769423589184.png",
            category = "Marketing",
            salary = "$98K",
            salaryPeriod = "year",
            salaryMin = 98000,
            location = "Toronto, ON",
            province = "Ontario",
            jobType = "Full-Time",
            isRemote = false,
            isNew = true,
            isFeatured = false,
            applicants = 22,
            applyUrl = "https://www.spotify.com/careers",
            description = "Lead growth campaigns that connect Canadian listeners and creators with the music they love. Utilize analytics and optimization to improve onboarding funnel performance.",
            skills = listOf("Performance Marketing", "Analytics", "SEO", "Paid Social"),
            postedDaysAgo = 1,
            applicantAvatars = listOf(
                "https://randomuser.me/api/portraits/men/20.jpg",
                "https://randomuser.me/api/portraits/women/21.jpg"
            )
        )
    )

    val provinces = listOf(
        "Ontario",
        "British Columbia",
        "Quebec",
        "Alberta",
        "Manitoba",
        "Saskatchewan",
        "Nova Scotia",
        "New Brunswick",
        "Newfoundland",
        "Prince Edward Island"
    )
}

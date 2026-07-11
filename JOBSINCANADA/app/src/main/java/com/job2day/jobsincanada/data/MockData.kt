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
        Company(1, "Shopify", "https://upload.wikimedia.org/wikipedia/commons/e/e1/Shopify_Logo.png", "https://www.shopify.com"),
        Company(2, "Wealthsimple", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Wealthsimple_logo.svg/512px-Wealthsimple_logo.svg.png", "https://www.wealthsimple.com"),
        Company(3, "Figma", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Figma-logo.svg/400px-Figma-logo.svg.png", "https://www.figma.com"),
        Company(4, "Microsoft", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/512px-Microsoft_logo.svg.png", "https://careers.microsoft.com"),
        Company(5, "Airbnb", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Airbnb_Logo_B%C3%A9lo.svg/512px-Airbnb_Logo_B%C3%A9lo.svg.png", "https://careers.airbnb.com"),
        Company(6, "Spotify", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Spotify_logo_without_text.svg/512px-Spotify_logo_without_text.svg.png", "https://www.spotify.com"),
        Company(7, "Apple", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/512px-Apple_logo_black.svg.png", "https://www.apple.com"),
        Company(8, "Slack", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/Slack_icon_2019.svg/512px-Slack_icon_2019.svg.png", "https://slack.com"),
        Company(9, "Notion", "https://upload.wikimedia.org/wikipedia/commons/e/e9/Notion-logo.png", "https://www.notion.so"),
        Company(10, "RBC", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Royal_Bank_of_Canada_Logo.svg/512px-Royal_Bank_of_Canada_Logo.svg.png", "https://www.rbc.com")
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
            companyLogo = "https://upload.wikimedia.org/wikipedia/commons/e/e1/Shopify_Logo.png",
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
            companyLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Wealthsimple_logo.svg/512px-Wealthsimple_logo.svg.png",
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
            companyLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Figma-logo.svg/400px-Figma-logo.svg.png",
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
            companyLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/512px-Microsoft_logo.svg.png",
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
            companyLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/Airbnb_Logo_B%C3%A9lo.svg/512px-Airbnb_Logo_B%C3%A9lo.svg.png",
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
            companyLogo = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Spotify_logo_without_text.svg/512px-Spotify_logo_without_text.svg.png",
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

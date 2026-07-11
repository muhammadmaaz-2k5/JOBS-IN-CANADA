package com.job2day.jobsincanada.data

data class Category(
    val id: Int,
    val label: String,
    val icon: String,
    val color: String,
    val count: Int = 0
)

data class Company(
    val id: Int,
    val name: String,
    val logoUrl: String,
    val website: String,
    val semanticLabel: String = ""
)

data class CareerResource(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: String,
    val color: String,
    val iconColor: String,
    val url: String = ""
)

data class JobListing(
    val id: Int,
    val title: String,
    val company: String,
    val companyLogo: String,
    val category: String,
    val salary: String,
    val salaryPeriod: String,
    val salaryMin: Int,
    val location: String,
    val province: String,
    val jobType: String,
    val isRemote: Boolean,
    val isNew: Boolean,
    val isFeatured: Boolean,
    val applicants: Int,
    val applyUrl: String,
    val description: String,
    val skills: List<String>,
    val postedDaysAgo: Int,
    val applicantAvatars: List<String> = emptyList(),
    var isSaved: Boolean = false
)

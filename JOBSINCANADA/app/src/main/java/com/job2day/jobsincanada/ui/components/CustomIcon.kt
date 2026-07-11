package com.job2day.jobsincanada.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconByName(name: String): ImageVector {
    return when (name.lowercase()) {
        "design_services_outlined" -> Icons.Outlined.DesignServices
        "campaign_outlined" -> Icons.Outlined.Campaign
        "code_rounded" -> Icons.Default.Code
        "inventory_2_outlined" -> Icons.Outlined.Inventory
        "bar_chart_rounded" -> Icons.Default.BarChart
        "account_balance_outlined" -> Icons.Outlined.AccountBalance
        "health_and_safety_outlined" -> Icons.Outlined.HealthAndSafety
        "gavel_rounded" -> Icons.Default.Gavel
        "trending_up_rounded" -> Icons.Default.TrendingUp
        "school_outlined" -> Icons.Outlined.School
        
        "description_outlined" -> Icons.Outlined.Description
        "chat_bubble_outline_rounded" -> Icons.Outlined.ChatBubbleOutline
        "insights_rounded" -> Icons.Default.Insights
        
        "location_on_outlined" -> Icons.Outlined.LocationOn
        "attach_money_rounded" -> Icons.Default.AttachMoney
        "schedule_outlined" -> Icons.Outlined.Schedule
        "workspace_premium_outlined" -> Icons.Outlined.WorkspacePremium
        
        "bookmark_outline_rounded" -> Icons.Outlined.BookmarkBorder
        "bookmark_rounded" -> Icons.Default.Bookmark
        "home_outlined" -> Icons.Outlined.Home
        "home_rounded" -> Icons.Default.Home
        "search_outlined" -> Icons.Outlined.Search
        "search_rounded" -> Icons.Default.Search
        "arrow_outward_rounded" -> Icons.Default.ArrowOutward
        
        else -> Icons.Default.HelpOutline
    }
}

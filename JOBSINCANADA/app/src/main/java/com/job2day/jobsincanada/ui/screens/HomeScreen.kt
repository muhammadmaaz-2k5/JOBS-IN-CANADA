package com.job2day.jobsincanada.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.job2day.jobsincanada.data.*
import com.job2day.jobsincanada.service.BookmarkService
import com.job2day.jobsincanada.ui.components.*
import com.job2day.jobsincanada.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToJobDetail: (JobListing) -> Unit,
    onNavigateToSearch: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var isLoading by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }

    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var companies by remember { mutableStateOf<List<Company>>(emptyList()) }
    var featuredJobs by remember { mutableStateOf<List<JobListing>>(emptyList()) }
    var recommendedJobs by remember { mutableStateOf<List<JobListing>>(emptyList()) }
    var careerResources by remember { mutableStateOf<List<CareerResource>>(emptyList()) }
    
    var jobsTodayCount by remember { mutableStateOf(0) }
    var jobsThisWeekCount by remember { mutableStateOf(0) }

    fun loadHomeData() {
        coroutineScope.launch {
            isLoading = true
            try {
                categories = ApiService.getCategories()
                companies = ApiService.getCompanies()
                
                val catFilter = if (selectedCategory == "All") null else selectedCategory
                
                // Concurrently fetch featured and recommended jobs
                val featuredResult = ApiService.getJobs(featured = true, category = catFilter, perPage = 10)
                featuredJobs = (featuredResult["data"] as? List<*>)?.mapNotNull { it as? JobListing } ?: emptyList()
                
                val recommendedResult = ApiService.getJobs(category = catFilter, perPage = 3)
                recommendedJobs = (recommendedResult["data"] as? List<*>)?.mapNotNull { it as? JobListing } ?: emptyList()
                
                careerResources = ApiService.getCareerResources()
                
                val settings = ApiService.getSettings()
                jobsTodayCount = settings["jobsToday"] ?: 0
                jobsThisWeekCount = settings["jobsThisWeek"] ?: 0

                // Resolve isSaved status from BookmarkService
                val savedIds = BookmarkService.getSavedJobIds(context)
                featuredJobs.forEach { it.isSaved = savedIds.contains(it.id) }
                recommendedJobs.forEach { it.isSaved = savedIds.contains(it.id) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(key1 = selectedCategory) {
        loadHomeData()
    }

    // Refresh view states when screen gains focus
    LaunchedEffect(key1 = true) {
        loadHomeData()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App Bar Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Profile image container
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Welcome Back",
                    style = Typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Muhammad Maaz",
                    style = Typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Notification Bell with badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
            }
        }

        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            
            // Welcome Section
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Find your",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Dream Job in Canada 🍁",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Input Trigger Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onNavigateToSearch(null) }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Search jobs, companies, keywords...",
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Horizontal Category Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 8.dp)
            ) {
                // "All" filter chip
                val allSelected = selectedCategory == "All"
                FilterChip(
                    selected = allSelected,
                    onClick = { selectedCategory = "All" },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Apps,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("All")
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                )

                // Other categories
                categories.forEach { cat ->
                    val isSel = selectedCategory == cat.label
                    val iconColor = contrastColorFor(cat.color, MaterialTheme.colorScheme.primary)
                    
                    FilterChip(
                        selected = isSel,
                        onClick = { selectedCategory = cat.label },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = getIconByName(cat.icon),
                                    contentDescription = null,
                                    tint = if (isSel) MaterialTheme.colorScheme.primary else iconColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(cat.label)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            // Stats Banner
            TodayJobsBanner(
                todayCount = jobsTodayCount,
                weekCount = jobsThisWeekCount
            )

            // 1. Featured Jobs Section
            if (featuredJobs.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Featured Jobs",
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "See All",
                        style = Typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToSearch(null) }
                    )
                }
                
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    featuredJobs.forEach { job ->
                        FeaturedJobCard(
                            job = job,
                            onTap = { onNavigateToJobDetail(job) },
                            onBookmarkToggle = {
                                BookmarkService.toggleSave(context, job.id)
                                loadHomeData()
                            }
                        )
                    }
                }
            }

            // 2. Recommended For You Section
            if (recommendedJobs.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Recommended For You",
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "See All",
                        style = Typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToSearch(null) }
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    recommendedJobs.forEach { job ->
                        RecommendedJobCard(
                            job = job,
                            onTap = { onNavigateToJobDetail(job) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 3. Top Companies Hiring Section
            if (companies.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Top Companies Hiring",
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "See All",
                        style = Typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToSearch(null) }
                    )
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    companies.forEach { company ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable { onNavigateToSearch(company.name) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(0.5.dp, MaterialTheme.colorScheme.outline, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                CompanyLogo(
                                    companyName = company.name,
                                    size = 56.dp,
                                    cornerRadius = 28.dp // fully circular fallback
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = company.name,
                                style = Typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 11.sp,
                                modifier = Modifier.width(64.dp)
                            )
                        }
                    }
                }
            }

            // 4. Job Categories Section
            if (categories.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Job Categories",
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "See All",
                        style = Typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToSearch(null) }
                    )
                }
                
                CategoryGrid(
                    categories = categories,
                    onCategoryTap = { catLabel ->
                        selectedCategory = catLabel
                    }
                )
            }

            // 5. Career Resources Section
            if (careerResources.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Career Resources",
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column {
                        careerResources.forEachIndexed { idx, res ->
                            val itemBg = parseHexColor(res.color, Color(0xFFF3F4F6))
                            val itemIconColor = parseHexColor(res.iconColor, MaterialTheme.colorScheme.primary)
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* Handle Resource Tap */ }
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(itemBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = getIconByName(res.icon),
                                        contentDescription = null,
                                        tint = itemIconColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = res.title,
                                        style = Typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = res.subtitle,
                                        style = Typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Icon(
                                    imageVector = Icons.Outlined.ChevronRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            
                            if (idx < careerResources.size - 1) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

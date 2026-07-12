package com.job2day.jobsincanada.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.job2day.jobsincanada.data.ApiService
import com.job2day.jobsincanada.data.Category
import com.job2day.jobsincanada.data.JobListing
import com.job2day.jobsincanada.service.BookmarkService
import androidx.compose.foundation.border
import com.job2day.jobsincanada.ui.components.AdmobNativeAd
import com.job2day.jobsincanada.ui.components.EmptyState
import com.job2day.jobsincanada.ui.components.FilterBottomSheet
import com.job2day.jobsincanada.ui.components.SearchJobCard
import com.job2day.jobsincanada.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSearchScreen(
    onNavigateToJobDetail: (JobListing) -> Unit,
    modifier: Modifier = Modifier,
    initialQuery: String? = null,
    onConsumeInitialQuery: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var query by remember { mutableStateOf(initialQuery ?: "") }
    var sortBy by remember { mutableStateOf("Relevance") }
    val sortOptions = listOf("Relevance", "Most Recent", "Highest Salary", "Most Applicants")

    var activeFilters by remember { mutableStateOf<Map<String, Any?>>(emptyMap()) }
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    
    val jobsList = remember { mutableStateListOf<JobListing>() }
    var currentPage by remember { mutableStateOf(1) }
    var lastPage by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var inputUrl by remember { mutableStateOf(ApiService.getBaseUrl()) }

    var isFilterOpen by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    fun fetchJobs(isRefresh: Boolean = false) {
        coroutineScope.launch {
            if (isRefresh) {
                currentPage = 1
                jobsList.clear()
            }
            isLoading = true
            errorMessage = null
            try {
                // Fetch categories for filter sheet
                if (categories.isEmpty()) {
                    categories = ApiService.getCategories()
                }

                // Gather params
                val category = activeFilters["category"] as? String
                val remote = activeFilters["remoteOnly"] as? Boolean
                val types = activeFilters["jobType"] as? List<*>
                val jobType = if (!types.isNullOrEmpty()) types.firstOrNull() as? String else null
                val provinces = activeFilters["province"] as? List<*>
                val province = if (!provinces.isNullOrEmpty()) provinces.firstOrNull() as? String else null
                val today = activeFilters["todayOnly"] as? Boolean
                val minSalary = if (activeFilters["highSalary"] == true) 100000 else null

                val results = ApiService.getJobs(
                    query = query,
                    category = category,
                    remote = remote,
                    type = jobType,
                    province = province,
                    today = today,
                    minSalary = minSalary,
                    page = currentPage,
                    perPage = 20
                )

                val data = (results["data"] as? List<*>)?.mapNotNull { it as? JobListing } ?: emptyList()
                lastPage = results["last_page"] as? Int ?: 1

                val savedIds = BookmarkService.getSavedJobIds(context)
                data.forEach { it.isSaved = savedIds.contains(it.id) }

                if (isRefresh) {
                    jobsList.clear()
                }
                jobsList.addAll(data)
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = e.message ?: "Failed to connect to the server."
            } finally {
                isLoading = false
            }
        }
    }

    // Debounce query changes
    LaunchedEffect(key1 = query) {
        delay(350)
        fetchJobs(isRefresh = true)
    }

    LaunchedEffect(key1 = initialQuery) {
        if (initialQuery != null) {
            if (initialQuery == "FILTER_TODAY") {
                query = ""
                activeFilters = mapOf("todayOnly" to true)
            } else {
                query = initialQuery
            }
            onConsumeInitialQuery()
        }
    }

    // Listen to scroll to load next page
    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= totalItems - 4 && totalItems > 0 && !isLoading && currentPage < lastPage
        }
    }
    
    LaunchedEffect(key1 = shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            currentPage++
            fetchJobs(isRefresh = false)
        }
    }

    // Load active saved states when returning/entering screen
    LaunchedEffect(key1 = true) {
        val savedIds = BookmarkService.getSavedJobIds(context)
        jobsList.forEach { it.isSaved = savedIds.contains(it.id) }
    }

    // Sorting implementation
    val sortedJobs = remember(jobsList, sortBy) {
        derivedStateOf {
            val list = jobsList.toList()
            when (sortBy) {
                "Most Recent" -> list.sortedBy { it.postedDaysAgo }
                "Highest Salary" -> list.sortedByDescending { it.salaryMin }
                "Most Applicants" -> list.sortedByDescending { it.applicants }
                else -> list
            }
        }
    }

    // Count active filter badges
    val activeFilterCount = remember(activeFilters) {
        var count = 0
        if (activeFilters["remoteOnly"] == true) count++
        if (activeFilters["todayOnly"] == true) count++
        if (activeFilters["highSalary"] == true) count++
        if (activeFilters["category"] != null) count++
        val types = activeFilters["jobType"] as? List<*>
        if (!types.isNullOrEmpty()) count++
        val provinces = activeFilters["province"] as? List<*>
        if (!provinces.isNullOrEmpty()) count++
        count
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Sticky Header Area
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Header Label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Browse Jobs",
                        style = Typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF111827)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${sortedJobs.value.size} results",
                        style = Typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Bar Input with Filter Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search jobs, companies...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // Filter Button with Badge
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable { isFilterOpen = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filters",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        if (activeFilterCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 10.dp, end = 10.dp)
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Sorting Menu & Quick Filters Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sorting dropdown chip
                    Box {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { showSortMenu = true }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = sortBy,
                                    style = Typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            sortOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        sortBy = opt
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Quick chip: Remote Only
                    val isRemoteSelected = activeFilters["remoteOnly"] == true
                    val remoteBg = if (isRemoteSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(remoteBg)
                            .clickable {
                                val nextFilters = activeFilters.toMutableMap()
                                nextFilters["remoteOnly"] = !isRemoteSelected
                                activeFilters = nextFilters
                                fetchJobs(isRefresh = true)
                            }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = "Remote Only",
                            style = Typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (isRemoteSelected) MaterialTheme.colorScheme.primary else Color(0xFF111827)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Quick chip: Today
                    val isTodaySelected = activeFilters["todayOnly"] == true
                    val todayBg = if (isTodaySelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(todayBg)
                            .clickable {
                                val nextFilters = activeFilters.toMutableMap()
                                nextFilters["todayOnly"] = !isTodaySelected
                                activeFilters = nextFilters
                                fetchJobs(isRefresh = true)
                            }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = "Today",
                            style = Typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (isTodaySelected) MaterialTheme.colorScheme.primary else Color(0xFF111827)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Quick chip: High Salary
                    val isHighSalarySelected = activeFilters["highSalary"] == true
                    val salaryBg = if (isHighSalarySelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(salaryBg)
                            .clickable {
                                val nextFilters = activeFilters.toMutableMap()
                                nextFilters["highSalary"] = !isHighSalarySelected
                                activeFilters = nextFilters
                                fetchJobs(isRefresh = true)
                            }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = "$100K+",
                            style = Typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (isHighSalarySelected) MaterialTheme.colorScheme.primary else Color(0xFF111827)
                        )
                    }
                }
            }
        }

        // Job Listings List
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val jobs = sortedJobs.value
            
            if (errorMessage != null && jobs.isEmpty()) {
                // Connection Error State
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .align(Alignment.Center)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp).fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.errorContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Connection Error",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Failed to connect to the Jobs in Canada API server. Make sure the Laravel backend is running and the URL is configured correctly.",
                            style = Typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = errorMessage ?: "",
                            style = Typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Current URL: ${ApiService.getBaseUrl()}",
                            style = Typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { 
                                    inputUrl = ApiService.getBaseUrl()
                                    showSettingsDialog = true 
                                },
                                shape = RoundedCornerShape(100.dp),
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text("Configure IP")
                            }

                            Button(
                                onClick = { fetchJobs(isRefresh = true) },
                                shape = RoundedCornerShape(100.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text("Try Again", color = Color.White)
                            }
                        }
                    }
                }
            } else if (isLoading && jobs.isEmpty()) {
                // Skeleton Loader
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                    }
                }
            } else if (jobs.isEmpty()) {
                // Empty State
                EmptyState(
                    icon = Icons.Outlined.SearchOff,
                    title = "No jobs found",
                    subtitle = "Try adjusting your search or filters to find Canadian jobs matching your skills.",
                    ctaLabel = "Clear Filters",
                    onCta = {
                        query = ""
                        activeFilters = emptyMap()
                        fetchJobs(isRefresh = true)
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(jobs) { index, job ->
                        SearchJobCard(
                            job = job,
                            onTap = { onNavigateToJobDetail(job) },
                            onBookmarkToggle = {
                                BookmarkService.toggleSave(context, job.id)
                                val savedIds = BookmarkService.getSavedJobIds(context)
                                jobs[index].isSaved = savedIds.contains(job.id)
                                // Trigger state refresh in list
                                jobsList[index] = jobs[index].copy(isSaved = savedIds.contains(job.id))
                            }
                        )

                        if (ApiService.adsEnabled && (index + 1) % 4 == 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            AdmobNativeAd(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White)
                                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    if (isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }

        // Filter Modal Bottom Sheet
        if (isFilterOpen) {
            FilterBottomSheet(
                currentFilters = activeFilters,
                categories = categories,
                onApply = { nextFilters ->
                    activeFilters = nextFilters
                    isFilterOpen = false
                    fetchJobs(isRefresh = true)
                },
                onDismissRequest = { isFilterOpen = false }
            )
        }

        // Settings Dialog
        if (showSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showSettingsDialog = false },
                title = { Text("API Server Configuration", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Specify the base API URL for the Jobs In Canada server:", fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = inputUrl,
                            onValueChange = { inputUrl = it },
                            label = { Text("Base URL") },
                            placeholder = { Text("e.g. http://10.0.2.2:8000/api") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            ApiService.updateBaseUrl(context, inputUrl)
                            showSettingsDialog = false
                            fetchJobs(isRefresh = true)
                        }
                    ) {
                        Text("Save & Retry")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSettingsDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

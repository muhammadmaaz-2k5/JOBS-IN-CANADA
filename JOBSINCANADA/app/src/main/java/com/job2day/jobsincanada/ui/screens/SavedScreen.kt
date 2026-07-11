package com.job2day.jobsincanada.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.job2day.jobsincanada.data.ApiService
import com.job2day.jobsincanada.data.JobListing
import com.job2day.jobsincanada.service.BookmarkService
import com.job2day.jobsincanada.ui.components.EmptyState
import com.job2day.jobsincanada.ui.components.SearchJobCard
import com.job2day.jobsincanada.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    onNavigateToJobDetail: (JobListing) -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    val savedJobs = remember { mutableStateListOf<JobListing>() }
    var isLoading by remember { mutableStateOf(false) }

    fun loadSavedJobs() {
        coroutineScope.launch {
            isLoading = true
            try {
                // Get all saved IDs
                val savedIds = BookmarkService.getSavedJobIds(context)
                if (savedIds.isEmpty()) {
                    savedJobs.clear()
                    return@launch
                }
                
                // Fetch jobs from repository and filter by saved status
                val results = ApiService.getJobs(page = 1, perPage = 100)
                val allJobs = (results["data"] as? List<*>)?.mapNotNull { it as? JobListing } ?: emptyList()
                
                val filtered = allJobs.filter { savedIds.contains(it.id) }
                filtered.forEach { it.isSaved = true }
                
                savedJobs.clear()
                savedJobs.addAll(filtered)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(key1 = true) {
        loadSavedJobs()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App Bar Header
        TopAppBar(
            title = {
                Text(
                    text = "Saved Jobs",
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            if (isLoading && savedJobs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (savedJobs.isEmpty()) {
                EmptyState(
                    icon = Icons.Outlined.BookmarkBorder,
                    title = "No saved jobs yet",
                    subtitle = "Keep track of jobs you're interested in by saving them.",
                    ctaLabel = "Find Jobs",
                    onCta = onNavigateToSearch,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Saved Count Label Header
                    val countText = if (savedJobs.size == 1) "1 job saved" else "${savedJobs.size} jobs saved"
                    Text(
                        text = countText,
                        style = Typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(savedJobs) { index, job ->
                            SearchJobCard(
                                job = job,
                                onTap = { onNavigateToJobDetail(job) },
                                onBookmarkToggle = {
                                    BookmarkService.toggleSave(context, job.id)
                                    // Remove immediately from saved jobs list
                                    savedJobs.removeAt(index)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

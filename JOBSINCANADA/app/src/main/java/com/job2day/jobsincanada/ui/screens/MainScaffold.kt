package com.job2day.jobsincanada.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.job2day.jobsincanada.data.JobListing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    onNavigateToJobDetail: (JobListing) -> Unit,
    modifier: Modifier = Modifier,
    initialTab: Int = 0
) {
    var selectedTab by remember { mutableStateOf(initialTab) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                // Home Tab
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                // Search Tab
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Filled.Search else Icons.Outlined.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Search") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                // Saved Tab
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Saved",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Saved") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    onNavigateToJobDetail = onNavigateToJobDetail,
                    onNavigateToSearch = { query ->
                        // Switch to search tab with initial search query
                        selectedTab = 1
                        // Wait, we can pass state to SearchScreen easily via a shared state in a real app,
                        // or by exposing simple flow/ViewModel. We'll handle it nicely inside MainActivity.
                    }
                )
                1 -> JobSearchScreen(
                    onNavigateToJobDetail = onNavigateToJobDetail
                )
                2 -> SavedScreen(
                    onNavigateToJobDetail = onNavigateToJobDetail,
                    onNavigateToSearch = { selectedTab = 1 }
                )
            }
        }
    }
}

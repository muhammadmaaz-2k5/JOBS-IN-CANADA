package com.job2day.jobsincanada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.job2day.jobsincanada.data.JobListing
import com.job2day.jobsincanada.ui.screens.JobDetailScreen
import com.job2day.jobsincanada.ui.screens.MainScaffold
import com.job2day.jobsincanada.ui.screens.SplashScreen
import com.job2day.jobsincanada.ui.theme.JOBSINCANADATheme

enum class AppScreen {
    Splash, Main, Detail
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.job2day.jobsincanada.data.ApiService.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            JOBSINCANADATheme {
                var currentScreen by remember { mutableStateOf(AppScreen.Splash) }
                var selectedJobDetail by remember { mutableStateOf<JobListing?>(null) }

                Crossfade(targetState = currentScreen, label = "screen_fade") { screen ->
                    when (screen) {
                        AppScreen.Splash -> {
                            SplashScreen(
                                onSplashComplete = {
                                    currentScreen = AppScreen.Main
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        AppScreen.Main -> {
                            MainScaffold(
                                onNavigateToJobDetail = { job ->
                                    selectedJobDetail = job
                                    currentScreen = AppScreen.Detail
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        AppScreen.Detail -> {
                            selectedJobDetail?.let { job ->
                                JobDetailScreen(
                                    job = job,
                                    onBackClick = {
                                        currentScreen = AppScreen.Main
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
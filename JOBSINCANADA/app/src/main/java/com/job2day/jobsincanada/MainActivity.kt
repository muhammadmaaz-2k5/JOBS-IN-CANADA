package com.job2day.jobsincanada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.job2day.jobsincanada.data.CareerResource
import com.job2day.jobsincanada.data.JobListing
import com.job2day.jobsincanada.ui.screens.CareerResourceDetailScreen
import com.job2day.jobsincanada.ui.screens.JobDetailScreen
import com.job2day.jobsincanada.ui.screens.MainScaffold
import com.job2day.jobsincanada.ui.screens.SplashScreen
import com.job2day.jobsincanada.ui.theme.JOBSINCANADATheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

enum class AppScreen {
    Splash, Main, Detail, ResourceDetail
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.job2day.jobsincanada.data.ApiService.initialize(applicationContext)
        lifecycleScope.launch {
            try {
                com.job2day.jobsincanada.data.ApiService.getSettings()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        try {
            val initConfig = com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig.Builder("ca-app-pub-3940256099942544~3347511713")
                .setNativeValidatorDisabled()
                .build()
            com.google.android.libraries.ads.mobile.sdk.MobileAds.initialize(applicationContext, initConfig)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        enableEdgeToEdge()
        setContent {
            JOBSINCANADATheme {
                var currentScreen by remember { mutableStateOf(AppScreen.Splash) }
                var selectedJobDetail by remember { mutableStateOf<JobListing?>(null) }
                var selectedResourceDetail by remember { mutableStateOf<CareerResource?>(null) }

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
                                onNavigateToResourceDetail = { res ->
                                    selectedResourceDetail = res
                                    currentScreen = AppScreen.ResourceDetail
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
                        AppScreen.ResourceDetail -> {
                            selectedResourceDetail?.let { res ->
                                CareerResourceDetailScreen(
                                    resource = res,
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
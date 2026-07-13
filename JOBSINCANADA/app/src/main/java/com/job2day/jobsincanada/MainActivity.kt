package com.job2day.jobsincanada

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.job2day.jobsincanada.data.CareerResource
import com.job2day.jobsincanada.data.JobListing
import com.job2day.jobsincanada.service.NotificationRouter
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
        
        createNotificationChannel()
        askNotificationPermission()

        try {
            FirebaseMessaging.getInstance().subscribeToTopic("all")
        } catch (e: Exception) {
            e.printStackTrace()
        }

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
        
        handleNotificationRoute(intent)
        enableEdgeToEdge()

        setContent {
            JOBSINCANADATheme {

                var currentScreen by remember { mutableStateOf(AppScreen.Splash) }
                var selectedJobDetail by remember { mutableStateOf<JobListing?>(null) }
                var selectedResourceDetail by remember { mutableStateOf<CareerResource?>(null) }
                var isLoadingDetail by remember { mutableStateOf(false) }

                LaunchedEffect(NotificationRouter.pendingRoute.value) {
                    val route = NotificationRouter.pendingRoute.value ?: return@LaunchedEffect
                    val data = NotificationRouter.pendingData.value

                    when (route) {
                        "home" -> {
                            currentScreen = AppScreen.Main
                        }
                        "detail" -> {
                            val jobIdStr = data?.get("job_id") ?: data?.get("id")
                            val jobId = jobIdStr?.toIntOrNull()
                            if (jobId != null) {
                                isLoadingDetail = true
                                try {
                                    val job = com.job2day.jobsincanada.data.ApiService.getJob(jobId)
                                    selectedJobDetail = job
                                    currentScreen = AppScreen.Detail
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                } finally {
                                    isLoadingDetail = false
                                }
                            }
                        }
                        "career_resource_detail" -> {
                            val resourceIdStr = data?.get("resource_id") ?: data?.get("id")
                            val resourceId = resourceIdStr?.toIntOrNull()
                            if (resourceId != null) {
                                isLoadingDetail = true
                                try {
                                    val resource = com.job2day.jobsincanada.data.ApiService.getCareerResourceById(resourceId)
                                    if (resource != null) {
                                        selectedResourceDetail = resource
                                        currentScreen = AppScreen.ResourceDetail
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                } finally {
                                    isLoadingDetail = false
                                }
                            }
                        }
                    }

                    // Reset to avoid duplicate handling
                    NotificationRouter.pendingRoute.value = null
                    NotificationRouter.pendingData.value = null
                }

                Box(modifier = Modifier.fillMaxSize()) {
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

                    if (isLoadingDetail) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x88000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF1A6B3C))
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationRoute(intent)
    }

    private fun handleNotificationRoute(intent: Intent?) {
        if (intent == null) return
        val route = intent.getStringExtra("route") ?: intent.getStringExtra("screen") ?: return
        intent.removeExtra("route")
        intent.removeExtra("screen")

        val data = mutableMapOf<String, String>()
        intent.extras?.keySet()?.forEach { key ->
            if (key != "route" && key != "screen") {
                intent.getStringExtra(key)?.let { value ->
                    data[key] = value
                }
            }
        }

        data.keys.forEach { key ->
            intent.removeExtra(key)
        }

        NotificationRouter.pendingData.value = data
        NotificationRouter.pendingRoute.value = route
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Default channel for app notifications"
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}
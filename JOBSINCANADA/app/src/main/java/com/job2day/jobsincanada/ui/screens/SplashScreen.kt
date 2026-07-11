package com.job2day.jobsincanada.ui.screens

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.job2day.jobsincanada.R

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isVideoLoaded by remember { mutableStateOf(false) }

    // Uri of the video inside res/raw/splashscreen.mp4
    val videoUri = remember {
        Uri.parse("android.resource://${context.packageName}/${R.raw.splashscreen}")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Embedded Android VideoView playing the raw video resource
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoURI(videoUri)
                    setOnPreparedListener { mp ->
                        mp.isLooping = false
                        start()
                        isVideoLoaded = true
                    }
                    setOnCompletionListener {
                        onSplashComplete()
                    }
                    setOnErrorListener { _, _, _ ->
                        onSplashComplete() // fallback to navigate on error
                        true
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading indicator shown until the video starts playing
        if (!isVideoLoaded) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Skip Button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onSplashComplete() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Skip",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

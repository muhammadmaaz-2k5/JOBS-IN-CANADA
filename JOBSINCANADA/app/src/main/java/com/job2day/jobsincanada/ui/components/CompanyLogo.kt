package com.job2day.jobsincanada.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage

@Composable
fun CompanyLogo(
    companyName: String,
    logoUrl: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    cornerRadius: Dp = 12.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        if (!logoUrl.isNullOrEmpty()) {
            SubcomposeAsyncImage(
                model = logoUrl,
                contentDescription = "$companyName logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE5E7EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                onError = { state ->
                    Log.e("CompanyLogo", "Failed to load logo for $companyName from URL '$logoUrl'. Error: ${state.result.throwable.message}", state.result.throwable)
                },
                error = {
                    FallbackLetterLogo(companyName, size)
                }
            )
        } else {
            FallbackLetterLogo(companyName, size)
        }
    }
}

@Composable
private fun FallbackLetterLogo(
    companyName: String,
    size: Dp
) {
    val bgColor = when (companyName.lowercase().trim()) {
        "shopify" -> Color(0xFF96BF48)
        "wealthsimple" -> Color(0xFF00D1C1)
        "figma" -> Color(0xFFF24E1E)
        "microsoft" -> Color(0xFF00A4EF)
        "airbnb" -> Color(0xFFFF5A5F)
        "spotify" -> Color(0xFF1DB954)
        "apple" -> Color(0xFF000000)
        "slack" -> Color(0xFF4A154B)
        "notion" -> Color(0xFF111111)
        "rbc" -> Color(0xFF005DAA)
        else -> Color(0xFF1A6B3C) // default primary brand green
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        val letter = if (companyName.isNotEmpty()) companyName.trim().substring(0, 1).uppercase() else "?"
        Text(
            text = letter,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.45).sp
        )
    }
}

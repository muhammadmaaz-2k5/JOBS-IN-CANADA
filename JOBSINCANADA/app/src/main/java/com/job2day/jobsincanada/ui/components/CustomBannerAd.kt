package com.job2day.jobsincanada.ui.components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.job2day.jobsincanada.utils.AdManager
import kotlinx.coroutines.delay

private val PrimaryColor = Color(0xFF1A6B3C)
private val SurfaceDarkColor = Color(0xFF1E2028)
private val SurfaceVariantDarkColor = Color(0xFF252830)
private val TextMutedColor = Color(0xFF9CA3AF)
private val TextPrimaryColor = Color(0xFFE6E6E6)
private val ErrorColor = Color(0xFFDC2626)

@Composable
fun InlineCardAd(
    placement: String = "generic",
    modifier: Modifier = Modifier,
    label: String = "Sponsored",
) {
    if (!AdManager.isAdPlacementEnabled(placement)) return

    Column(modifier = modifier) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                color = TextMutedColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
            )
        }
        CustomSmallCardAd(
            adUrl = AdManager.getAdPlacementUrl(placement),
            modifier = Modifier
                .width(140.dp)
                .height(200.dp),
            showClose = false,
        )
    }
}

@Composable
fun InlineBannerAd(
    placement: String = "generic",
    modifier: Modifier = Modifier,
) {
    if (!AdManager.isAdPlacementEnabled(placement)) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center,
    ) {
        CustomSmallCardAd(
            adUrl = AdManager.getAdPlacementUrl(placement),
            modifier = Modifier
                .width(100.dp)
                .height(100.dp),
            showClose = false,
        )
    }
}

@Composable
fun FullWidthAdBanner(
    placement: String = "generic",
    modifier: Modifier = Modifier,
) {
    if (!AdManager.isAdPlacementEnabled(placement)) return

    CustomBannerAd(
        adUrl = AdManager.getAdPlacementUrl(placement),
        modifier = modifier.fillMaxWidth(),
        alwaysExpanded = true,
    )
}

@Composable
fun CustomSmallCardAd(
    adUrl: String = AdManager.webviewAdUrl,
    backgroundColor: Color = SurfaceVariantDarkColor,
    modifier: Modifier = Modifier,
    showClose: Boolean = true,
) {
    if (!AdManager.isAdsEnabled || !AdManager.isWebviewAdsEnabled) {
        return
    }

    var isVisible by remember { mutableStateOf(true) }
    
    if (isVisible) {
        Box(
            modifier = modifier
                .background(backgroundColor, RoundedCornerShape(12.dp)),
        ) {
            DynamicWebView(
                url = adUrl,
                modifier = Modifier.fillMaxSize(),
                height = null,
                wrapInCard = false
            )
            
            if (showClose) {
                IconButton(
                    onClick = { isVisible = false },
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Ad",
                        tint = TextMutedColor,
                    )
                }
            }
        }
    }
}

@Composable
fun CustomBannerAd(
    adUrl: String = AdManager.webviewAdUrl,
    backgroundColor: Color = SurfaceVariantDarkColor,
    modifier: Modifier = Modifier,
    alwaysExpanded: Boolean = true,
) {
    if (!AdManager.isAdsEnabled || !AdManager.isWebviewAdsEnabled) {
        return
    }

    var isVisible by remember { mutableStateOf(true) }
    var isExpanded by remember { mutableStateOf(alwaysExpanded) }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            color = backgroundColor,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color.White.copy(alpha = 0.1f)
            )
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(SurfaceDarkColor)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Sponsored",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMutedColor
                    )
                    
                    Row {
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = TextMutedColor
                            )
                        }
                        
                        IconButton(
                            onClick = { isVisible = false }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Ad",
                                tint = TextMutedColor
                            )
                        }
                    }
                }
                
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        CollapsibleWebView(
                            url = adUrl,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CollapsibleWebView(
    url: String,
    modifier: Modifier = Modifier,
) {
    DynamicWebView(
        url = url,
        modifier = modifier,
        height = null,
        wrapInCard = false
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CustomInterstitialAd(
    adUrl: String = AdManager.webviewAdUrl,
    countdownSeconds: Int = 10,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!AdManager.isAdsEnabled || !AdManager.isWebviewAdsEnabled) {
        onDismiss()
        return
    }

    if (adUrl.isBlank()) {
        onDismiss()
        return
    }
    
    var countdown by remember { mutableStateOf(countdownSeconds) }
    var isLoading by remember { mutableStateOf(true) }
    val currentOnDismiss by rememberUpdatedState(onDismiss)

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
    }

    Dialog(
        onDismissRequest = {
            if (countdown == 0) {
                currentOnDismiss()
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = countdown == 0,
            dismissOnClickOutside = countdown == 0,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.85f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceDarkColor)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(SurfaceVariantDarkColor)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Advertisement",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimaryColor
                        )
                        
                        Box(
                            modifier = Modifier
                                .background(
                                    if (countdown > 0) PrimaryColor else ErrorColor,
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            if (countdown > 0) {
                                Text(
                                    text = "Skip in ${countdown}s",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Close",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        InterstitialWebView(
                            url = adUrl,
                            modifier = Modifier.fillMaxSize(),
                            onPageLoaded = { isLoading = false }
                        )
                        
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = PrimaryColor
                            )
                        }
                    }
                }
                
                if (countdown == 0) {
                    IconButton(
                        onClick = currentOnDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(ErrorColor, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Ad",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun InterstitialWebView(
    url: String,
    modifier: Modifier = Modifier,
    onPageLoaded: (() -> Unit)? = null,
) {
    DynamicWebView(
        url = url,
        modifier = modifier,
        height = null,
        onPageLoaded = onPageLoaded,
        wrapInCard = false
    )
}

@Composable
fun AdCardRow(
    placements: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        placements.forEach { placement ->
            if (AdManager.isAdPlacementEnabled(placement)) {
                CustomSmallCardAd(
                    adUrl = AdManager.getAdPlacementUrl(placement),
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    showClose = false
                )
            }
        }
    }
}


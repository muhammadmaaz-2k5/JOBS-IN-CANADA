package com.job2day.jobsincanada.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.nativead.*
import com.job2day.jobsincanada.R
import com.job2day.jobsincanada.data.ApiService

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/9214589741" // Test Banner ID
) {
    if (!ApiService.adsEnabled) return

    val context = LocalContext.current
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    var bannerAd by remember { mutableStateOf<BannerAd?>(null) }

    DisposableEffect(key1 = adUnitId, key2 = screenWidth) {
        val extras = Bundle().apply {
            putString("collapsible", "bottom")
        }

        val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidth)
        val adRequest = BannerAdRequest.Builder(adUnitId, adSize)
            .setGoogleExtrasBundle(extras)
            .build()

        BannerAd.load(
            adRequest,
            object : AdLoadCallback<BannerAd> {
                override fun onAdLoaded(ad: BannerAd) {
                    bannerAd = ad
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Fail silently
                }
            }
        )

        onDispose {
            // Clean up resources if necessary
        }
    }

    bannerAd?.let { ad ->
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            factory = { ctx ->
                FrameLayout(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    val activity = ctx.findActivity()
                    if (activity != null) {
                        val adView = ad.getView(activity)
                        (adView.parent as? ViewGroup)?.removeView(adView)
                        addView(adView)
                    }
                }
            }
        )
    }
}

@Composable
fun AdmobNativeAd(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/2247696110" // Test Native Ad Unit
) {
    if (!ApiService.adsEnabled) return

    val context = LocalContext.current
    var loadedAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(key1 = adUnitId) {
        val adRequest = NativeAdRequest.Builder(adUnitId, listOf(NativeAd.NativeAdType.NATIVE))
            .build()

        val callback = object : NativeAdLoaderCallback {
            override fun onNativeAdLoaded(ad: NativeAd) {
                loadedAd = ad
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // Fail silently
            }
        }

        NativeAdLoader.load(adRequest, callback)

        onDispose {
            // Clean up resources if necessary
        }
    }

    loadedAd?.let { nativeAd ->
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            factory = { ctx ->
                val adView = LayoutInflater.from(ctx).inflate(R.layout.native_ad, null) as NativeAdView
                populateNativeAd(nativeAd, adView)
                adView
            }
        )
    }
}

private fun populateNativeAd(
    nativeAd: NativeAd,
    adView: NativeAdView
) {
    val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
    val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
    val bodyView = adView.findViewById<TextView>(R.id.ad_body)
    val callToActionView = adView.findViewById<Button>(R.id.ad_call_to_action)

    headlineView?.text = nativeAd.headline
    bodyView?.text = nativeAd.body
    callToActionView?.text = nativeAd.callToAction

    adView.registerNativeAd(nativeAd, mediaView)
}

package com.job2day.jobsincanada.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AdManager {
    private const val TAG = "AdManager"
    private const val INTERSTITIAL_COOLDOWN_MS = 45_000L
    private const val MAX_INTERSTITIALS_PER_SESSION = 6

    const val DEFAULT_WEBVIEW_AD_URL = "https://nazaarabox.com"

    var isAdsEnabled: Boolean = false
        private set

    var isWebviewAdsEnabled: Boolean = false
        private set

    var webviewAdUrl: String = DEFAULT_WEBVIEW_AD_URL
        private set

    val popupWebviewUrl: String
        get() = webviewAdUrl



    var isShowingAd = false
        private set

    @Volatile
    private var lastInterstitialAt: Long = 0L

    @Volatile
    private var interstitialCount: Int = 0

    private val _showInterstitial = MutableStateFlow(false)
    val showInterstitial: StateFlow<Boolean> = _showInterstitial.asStateFlow()

    private var pendingDismissCallback: (() -> Unit)? = null

    private var rawSettings: Map<String, String> = emptyMap()

    fun applySettings(settings: Map<String, String>) {
        rawSettings = settings
        isAdsEnabled = parseBoolean(settings["ads_enabled"])
        isWebviewAdsEnabled = parseBoolean(settings["enable_webview_ads"])
        webviewAdUrl = settings["webview_ad_url"]?.trim()?.takeIf { it.isNotBlank() }
            ?: DEFAULT_WEBVIEW_AD_URL
        Log.d(
            TAG,
            "Settings applied: ads=$isAdsEnabled, webview=$isWebviewAdsEnabled, url=$webviewAdUrl",
        )
    }

    fun isAdPlacementEnabled(placement: String): Boolean {
        if (!isAdsEnabled || !isWebviewAdsEnabled) return false
        val specificToggle = rawSettings["enable_ad_$placement"]
        return if (specificToggle != null) {
            parseBoolean(specificToggle)
        } else {
            true
        }
    }

    fun getAdPlacementUrl(placement: String): String {
        val specificUrl = rawSettings["ad_url_$placement"]?.trim()
        return if (!specificUrl.isNullOrBlank()) {
            specificUrl
        } else {
            webviewAdUrl
        }
    }

    /** @deprecated Use [applySettings] instead */
    fun setAdUnitIds(settings: Map<String, String>) {
        applySettings(settings)
    }

    private fun parseBoolean(value: String?): Boolean {
        if (value == null) return false
        return when (value.lowercase()) {
            "true", "1", "yes", "on" -> true
            else -> false
        }
    }

    fun initialize(context: Context) {
        if (!isAdsEnabled) return
    }

    fun loadInterstitial(context: Context) {
    }

    @Synchronized
    fun canShowInterstitial(): Boolean {
        if (!isAdsEnabled || !isWebviewAdsEnabled) return false
        if (interstitialCount >= MAX_INTERSTITIALS_PER_SESSION) return false
        val now = System.currentTimeMillis()
        return (now - lastInterstitialAt > INTERSTITIAL_COOLDOWN_MS)
    }

    @Synchronized
    fun recordInterstitial() {
        lastInterstitialAt = System.currentTimeMillis()
        interstitialCount++
    }

    fun isInterstitialAdReady(): Boolean =
        isAdsEnabled && isWebviewAdsEnabled && webviewAdUrl.isNotBlank()

    fun showInterstitial(activity: Activity, onAdDismissed: () -> Unit) {
        showWebviewAd(activity, onAdDismissed)
    }

    fun showWebviewAd(activity: Activity, onAdDismissed: () -> Unit) {
        onAdDismissed()
    }

    fun dismissInterstitial() {
        _showInterstitial.value = false
        isShowingAd = false
        pendingDismissCallback?.invoke()
        pendingDismissCallback = null
    }

    fun showAdMobInterstitialOnly(activity: Activity, onAdDismissed: () -> Unit) {
        showWebviewAd(activity, onAdDismissed)
    }

    fun showTmdbAd(activity: Activity, onAdDismissed: () -> Unit) {
        showWebviewAd(activity, onAdDismissed)
    }

    fun showOwnDramaAd(activity: Activity, requiredAdType: String, onAdDismissed: () -> Unit) {
        showWebviewAd(activity, onAdDismissed)
    }

    fun loadRewarded(context: Context) {
    }

    fun showRewarded(activity: Activity, onUserEarnedReward: () -> Unit, onAdDismissed: () -> Unit) {
        onUserEarnedReward()
        onAdDismissed()
    }

    fun loadAppOpenAd(context: Context) {
    }

    fun showAppOpenAd(activity: Activity, onAdDismissed: () -> Unit = {}) {
        showWebviewAd(activity, onAdDismissed)
    }

    fun loadRewardedInterstitial(context: Context) {
    }

    fun showRewardedInterstitial(activity: Activity, onUserEarnedReward: () -> Unit, onAdDismissed: () -> Unit) {
        onUserEarnedReward()
        onAdDismissed()
    }
}

import 'dart:developer';
import 'package:flutter/foundation.dart';

class AdManager {
  static const String tag = 'AdManager';
  static const int interstitialCooldownMs = 45000;
  static const int maxInterstitialsPerSession = 6;
  static const String defaultWebviewAdUrl = 'https://nazaarabox.com';

  static bool isAdsEnabled = false;
  static bool isWebviewAdsEnabled = false;
  static String webviewAdUrl = defaultWebviewAdUrl;
  static String appMode = 'live';
  static bool isSafeMode = true;
  static bool isShowingAd = false;

  static int _lastInterstitialAt = 0;
  static int _interstitialCount = 0;

  // ValueNotifier behaves like Compose's MutableState / Kotlin's Flow for triggering UI
  static final showInterstitialNotifier = ValueNotifier<bool>(false);
  
  static String get popupWebviewUrl => webviewAdUrl;
  static bool get isLiveMode => appMode == 'live';
  static bool get isSafeReviewMode => appMode == 'safe_review';

  static Map<String, String> _rawSettings = {};

  static void applySettings(Map<String, String> settings) {
    _rawSettings = settings;
    isAdsEnabled = _parseBoolean(settings['ads_enabled']);
    isWebviewAdsEnabled = _parseBoolean(settings['enable_webview_ads']);
    
    final url = settings['webview_ad_url']?.trim();
    webviewAdUrl = (url != null && url.isNotEmpty) ? url : defaultWebviewAdUrl;

    final modeValue = settings['app_mode']?.trim().toLowerCase();
    appMode = (modeValue == 'live' || modeValue == 'safe_review') ? modeValue! : 'live';
    isSafeMode = appMode == 'safe_review';

    log(
      'Settings applied: ads=$isAdsEnabled, webview=$isWebviewAdsEnabled, url=$webviewAdUrl, appMode=$appMode, safeMode=$isSafeMode',
      name: tag
    );
  }

  static bool isAdPlacementEnabled(String placement) {
    if (!isAdsEnabled || !isWebviewAdsEnabled) return false;
    final specificToggle = _rawSettings['enable_ad_$placement'];
    if (specificToggle != null) {
      return _parseBoolean(specificToggle);
    }
    return true;
  }

  static String getAdPlacementUrl(String placement) {
    final specificUrl = _rawSettings['ad_url_$placement']?.trim();
    if (specificUrl != null && specificUrl.isNotEmpty) {
      return specificUrl;
    }
    return webviewAdUrl;
  }

  static void setAdUnitIds(Map<String, String> settings) {
    applySettings(settings);
  }

  static bool _parseBoolean(String? value) {
    if (value == null) return false;
    final lower = value.toLowerCase();
    return lower == 'true' || lower == '1' || lower == 'yes' || lower == 'on';
  }

  static void initialize() {
    if (!isAdsEnabled) return;
  }

  static void loadInterstitial() {}

  static bool canShowInterstitial() {
    if (!isAdsEnabled || !isWebviewAdsEnabled) return false;
    if (_interstitialCount >= maxInterstitialsPerSession) return false;
    final now = DateTime.now().millisecondsSinceEpoch;
    return (now - _lastInterstitialAt > interstitialCooldownMs);
  }

  static void recordInterstitial() {
    _lastInterstitialAt = DateTime.now().millisecondsSinceEpoch;
    _interstitialCount++;
  }

  static bool isInterstitialAdReady() {
    return isAdsEnabled && isWebviewAdsEnabled && webviewAdUrl.isNotEmpty;
  }

  static void showInterstitial(void Function() onAdDismissed) {
    showWebviewAd(onAdDismissed);
  }

  static void showWebviewAd(void Function() onAdDismissed) {
    if (!isInterstitialAdReady()) {
      onAdDismissed();
      return;
    }
    if (isShowingAd) {
      onAdDismissed();
      return;
    }
    if (!canShowInterstitial()) {
      onAdDismissed();
      return;
    }
    _pendingDismissCallback = onAdDismissed;
    isShowingAd = true;
    recordInterstitial();
    showInterstitialNotifier.value = true;
  }

  static void Function()? _pendingDismissCallback;

  static void dismissInterstitial() {
    showInterstitialNotifier.value = false;
    isShowingAd = false;
    _pendingDismissCallback?.call();
    _pendingDismissCallback = null;
  }

  static void showAdMobInterstitialOnly(void Function() onAdDismissed) {
    showWebviewAd(onAdDismissed);
  }

  static void showTmdbAd(void Function() onAdDismissed) {
    showWebviewAd(onAdDismissed);
  }

  static void showOwnDramaAd(String requiredAdType, void Function() onAdDismissed) {
    showWebviewAd(onAdDismissed);
  }

  static void loadRewarded() {}

  static void showRewarded(void Function() onUserEarnedReward, void Function() onAdDismissed) {
    onUserEarnedReward();
    onAdDismissed();
  }

  static void loadAppOpenAd() {}

  static void showAppOpenAd(void Function() onAdDismissed) {
    showWebviewAd(onAdDismissed);
  }

  static void loadRewardedInterstitial() {}

  static void showRewardedInterstitial(void Function() onUserEarnedReward, void Function() onAdDismissed) {
    onUserEarnedReward();
    onAdDismissed();
  }
}

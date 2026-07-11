import 'dart:async';
import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:jobincanada/theme/app_theme.dart';
import 'package:jobincanada/services/ad_manager.dart';
import 'package:jobincanada/widgets/dynamic_webview.dart';

// --- CustomBannerAd ---
class CustomBannerAd extends StatefulWidget {
  final String adUrl;
  final Color backgroundColor;
  final bool alwaysExpanded;

  const CustomBannerAd({
    Key? key,
    this.adUrl = AdManager.defaultWebviewAdUrl,
    this.backgroundColor = AppTheme.surfaceVariantDark,
    this.alwaysExpanded = true,
  }) : super(key: key);

  @override
  State<CustomBannerAd> createState() => _CustomBannerAdState();
}

class _CustomBannerAdState extends State<CustomBannerAd> {
  bool _isVisible = true;
  late bool _isExpanded;

  @override
  void initState() {
    super.initState();
    _isExpanded = widget.alwaysExpanded;
  }

  @override
  Widget build(BuildContext context) {
    if (!AdManager.isAdsEnabled || !AdManager.isWebviewAdsEnabled || !_isVisible) {
      return const SizedBox.shrink();
    }

    return AnimatedCrossFade(
      firstChild: const SizedBox.shrink(),
      secondChild: Card(
        color: widget.backgroundColor,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12.0)),
        clipBehavior: Clip.antiAlias,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            // Header Bar
            Container(
              height: 48.0,
              color: AppTheme.surfaceDark,
              padding: const EdgeInsets.symmetric(horizontal: 12.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    'Sponsored',
                    style: TextStyle(color: AppTheme.textMuted, fontSize: 11.0, fontWeight: FontWeight.bold),
                  ),
                  Row(
                    children: [
                      IconButton(
                        onPressed: () {
                          setState(() {
                            _isExpanded = !_isExpanded;
                          });
                        },
                        icon: Icon(
                          _isExpanded ? Icons.arrow_drop_up : Icons.arrow_drop_down,
                          color: AppTheme.textMuted,
                        ),
                      ),
                      IconButton(
                        onPressed: () {
                          setState(() {
                            _isVisible = false;
                          });
                        },
                        icon: const Icon(Icons.close, color: AppTheme.textMuted),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            // Collapsible Content
            AnimatedSize(
              duration: const Duration(milliseconds: 300),
              curve: Curves.easeInOut,
              child: _isExpanded
                  ? SizedBox(
                      height: 250.0,
                      child: CollapsibleWebView(url: widget.adUrl),
                    )
                  : const SizedBox.shrink(),
            ),
          ],
        ),
      ),
      crossFadeState: _isVisible ? CrossFadeState.showSecond : CrossFadeState.showFirst,
      duration: const Duration(milliseconds: 200),
    );
  }
}

// --- CollapsibleWebView ---
class CollapsibleWebView extends StatelessWidget {
  final String url;

  const CollapsibleWebView({
    Key? key,
    required this.url,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return DynamicWebView(
      url: url,
      height: null,
      autoClickDelayMs: 2000,
      autoClickIntervalMs: 2000,
      clickYFraction: 0.5,
      wrapInCard: false,
    );
  }
}

// --- CustomInterstitialAd ---
class CustomInterstitialAd extends StatefulWidget {
  final String adUrl;
  final int countdownSeconds;
  final VoidCallback onDismiss;

  const CustomInterstitialAd({
    Key? key,
    this.adUrl = AdManager.defaultWebviewAdUrl,
    this.countdownSeconds = 10,
    required this.onDismiss,
  }) : super(key: key);

  @override
  State<CustomInterstitialAd> createState() => _CustomInterstitialAdState();
}

class _CustomInterstitialAdState extends State<CustomInterstitialAd> {
  late int _countdown;
  bool _isLoading = true;
  Timer? _timer;

  @override
  void initState() {
    super.initState();
    _countdown = widget.countdownSeconds;
    _startCountdown();
  }

  void _startCountdown() {
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_countdown > 0) {
        setState(() {
          _countdown--;
        });
      } else {
        _timer?.cancel();
      }
    });
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (!AdManager.isAdsEnabled || !AdManager.isWebviewAdsEnabled || widget.adUrl.isEmpty) {
      WidgetsBinding.instance.addPostFrameCallback((_) => widget.onDismiss());
      return const SizedBox.shrink();
    }

    return WillPopScope(
      onWillPop: () async => _countdown == 0,
      child: Dialog(
        insetPadding: EdgeInsets.zero,
        backgroundColor: Colors.black.withOpacity(0.7),
        child: Center(
          child: Container(
            width: MediaQuery.of(context).size.width * 0.95,
            height: MediaQuery.of(context).size.height * 0.85,
            decoration: BoxDecoration(
              color: AppTheme.surfaceDark,
              borderRadius: BorderRadius.circular(16.0),
            ),
            clipBehavior: Clip.antiAlias,
            child: Column(
              children: [
                // Header
                Container(
                  height: 56.0,
                  color: AppTheme.surfaceVariantDark,
                  padding: const EdgeInsets.symmetric(horizontal: 16.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      const Text(
                        'Advertisement',
                        style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                      GestureDetector(
                        onTap: _countdown == 0 ? widget.onDismiss : null,
                        child: Container(
                          decoration: BoxDecoration(
                            color: _countdown > 0 ? AppTheme.primary : AppTheme.errorColor,
                            borderRadius: BorderRadius.circular(20.0),
                          ),
                          padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
                          child: _countdown > 0
                              ? Text(
                                  'Skip in ${_countdown}s',
                                  style: const TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold),
                                )
                              : const Row(
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    Icon(Icons.close, color: Colors.white, size: 18),
                                    SizedBox(width: 4.0),
                                    Text(
                                      'Close',
                                      style: TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold),
                                    ),
                                  ],
                                ),
                        ),
                      ),
                    ],
                  ),
                ),
                // WebView Container
                Expanded(
                  child: Stack(
                    children: [
                      DynamicWebView(
                        url: widget.adUrl,
                        height: null,
                        onPageLoaded: () {
                          setState(() {
                            _isLoading = false;
                          });
                        },
                        autoClickDelayMs: 2000,
                        autoClickIntervalMs: 2000,
                        clickYFraction: 0.5,
                        wrapInCard: false,
                      ),
                      if (_isLoading)
                        const Center(
                          child: CircularProgressIndicator(color: AppTheme.primary),
                        ),
                      if (_countdown == 0)
                        Positioned(
                          top: 8.0,
                          right: 8.0,
                          child: IconButton(
                            onPressed: widget.onDismiss,
                            icon: Container(
                              padding: const EdgeInsets.all(4.0),
                              decoration: const BoxDecoration(
                                color: AppTheme.errorColor,
                                shape: BoxShape.circle,
                              ),
                              child: const Icon(Icons.close, color: Colors.white, size: 20),
                            ),
                          ),
                        ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

// --- NativeAdItem Data Class ---
class NativeAdItem {
  final int id;
  final String title;
  final String description;
  final String imageUrl;
  final String buttonText;
  final String buttonLink;
  final String buttonColor;

  const NativeAdItem({
    this.id = 0,
    this.title = '',
    this.description = '',
    this.imageUrl = '',
    this.buttonText = 'Learn More',
    this.buttonLink = '',
    this.buttonColor = '#FF6B6B',
  });
}

// --- CustomNativeAd ---
class CustomNativeAd extends StatefulWidget {
  final NativeAdItem? ad;
  final String adUrl;
  final Color backgroundColor;

  const CustomNativeAd({
    Key? key,
    this.ad,
    this.adUrl = AdManager.defaultWebviewAdUrl,
    this.backgroundColor = AppTheme.surfaceVariantDark,
  }) : super(key: key);

  @override
  State<CustomNativeAd> createState() => _CustomNativeAdState();
}

class _CustomNativeAdState extends State<CustomNativeAd> {
  bool _isVisible = true;

  @override
  Widget build(BuildContext context) {
    if (!AdManager.isAdsEnabled || !_isVisible) {
      return const SizedBox.shrink();
    }

    final adData = widget.ad ?? NativeAdItem(
      title: 'Sponsored Content',
      description: 'Check out this amazing offer!',
      imageUrl: 'https://nazaarabox.com/logo.png',
      buttonText: 'Learn More',
      buttonLink: widget.adUrl,
    );

    return Container(
      height: 100.0,
      margin: const EdgeInsets.all(8.0),
      decoration: BoxDecoration(
        color: widget.backgroundColor,
        borderRadius: BorderRadius.circular(12.0),
      ),
      clipBehavior: Clip.antiAlias,
      child: Stack(
        children: [
          Row(
            children: [
              // Image on left
              if (adData.imageUrl.isNotEmpty)
                CachedNetworkImage(
                  imageUrl: adData.imageUrl,
                  width: 100.0,
                  height: 100.0,
                  fit: BoxFit.cover,
                  errorWidget: (context, url, error) => const Icon(Icons.broken_image, color: AppTheme.textMuted),
                ),
              // Content on right
              Expanded(
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        adData.title,
                        style: const TextStyle(color: Colors.white, fontSize: 14.0, fontWeight: FontWeight.bold),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                      const SizedBox(height: 4.0),
                      Text(
                        adData.description,
                        style: const TextStyle(color: AppTheme.textMuted, fontSize: 12.0),
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
          Positioned(
            top: 2.0,
            right: 2.0,
            child: IconButton(
              onPressed: () {
                setState(() {
                  _isVisible = false;
                });
              },
              icon: const Icon(Icons.close, color: AppTheme.textMuted, size: 18),
            ),
          ),
        ],
      ),
    );
  }
}

// --- CustomSmallCardAd ---
class CustomSmallCardAd extends StatefulWidget {
  final String adUrl;
  final Color backgroundColor;
  final bool showClose;

  const CustomSmallCardAd({
    Key? key,
    this.adUrl = AdManager.defaultWebviewAdUrl,
    this.backgroundColor = AppTheme.surfaceVariantDark,
    this.showClose = true,
  }) : super(key: key);

  @override
  State<CustomSmallCardAd> createState() => _CustomSmallCardAdState();
}

class _CustomSmallCardAdState extends State<CustomSmallCardAd> {
  bool _isVisible = true;

  @override
  Widget build(BuildContext context) {
    if (!AdManager.isAdsEnabled || !AdManager.isWebviewAdsEnabled || !_isVisible) {
      return const SizedBox.shrink();
    }

    return Container(
      decoration: BoxDecoration(
        color: widget.backgroundColor,
        borderRadius: BorderRadius.circular(12.0),
      ),
      clipBehavior: Clip.antiAlias,
      child: Stack(
        children: [
          DynamicWebView(
            url: widget.adUrl,
            height: null,
            autoClickDelayMs: 2000,
            autoClickIntervalMs: 2000,
            clickYFraction: 0.5,
            wrapInCard: false,
          ),
          if (widget.showClose)
            Positioned(
              top: 2.0,
              right: 2.0,
              child: IconButton(
                onPressed: () {
                  setState(() {
                    _isVisible = false;
                  });
                },
                icon: const Icon(Icons.close, color: AppTheme.textMuted, size: 18),
              ),
            ),
        ],
      ),
    );
  }
}

// --- InlineCardAd ---
class InlineCardAd extends StatelessWidget {
  final String placement;
  final String label;

  const InlineCardAd({
    Key? key,
    this.placement = 'generic',
    this.label = 'Sponsored',
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    if (!AdManager.isAdPlacementEnabled(placement)) {
      return const SizedBox.shrink();
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisSize: MainAxisSize.min,
      children: [
        if (label.isNotEmpty)
          Padding(
            padding: const EdgeInsets.only(left: 4.0, bottom: 4.0),
            child: Text(
              label,
              style: const TextStyle(color: AppTheme.textMuted, fontSize: 10.0, fontWeight: FontWeight.bold),
            ),
          ),
        SizedBox(
          width: 140.0,
          height: 200.0,
          child: CustomSmallCardAd(
            adUrl: AdManager.getAdPlacementUrl(placement),
            showClose: false,
          ),
        ),
      ],
    );
  }
}

// --- InlineBannerAd ---
class InlineBannerAd extends StatelessWidget {
  final String placement;

  const InlineBannerAd({
    Key? key,
    this.placement = 'generic',
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    if (!AdManager.isAdPlacementEnabled(placement)) {
      return const SizedBox.shrink();
    }

    return Container(
      width: double.infinity,
      height: 110.0,
      decoration: BoxDecoration(
        color: AppTheme.surfaceVariantDark,
        borderRadius: BorderRadius.circular(12.0),
      ),
      child: Center(
        child: CustomBannerAd(
          adUrl: AdManager.getAdPlacementUrl(placement),
          alwaysExpanded: true,
        ),
      ),
    );
  }
}

// --- FullWidthAdBanner ---
class FullWidthAdBanner extends StatelessWidget {
  final String placement;

  const FullWidthAdBanner({
    Key? key,
    this.placement = 'generic',
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    if (!AdManager.isAdPlacementEnabled(placement)) {
      return const SizedBox.shrink();
    }

    return CustomBannerAd(
      adUrl: AdManager.getAdPlacementUrl(placement),
      alwaysExpanded: true,
    );
  }
}

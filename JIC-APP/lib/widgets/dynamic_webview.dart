import 'dart:async';
import 'dart:developer';
import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:jobincanada/theme/app_theme.dart';

String _buildInjectionScript(String userScript, String? readySelector) {
  if (readySelector != null && readySelector.trim().isNotEmpty) {
    final safeSelector = readySelector.replaceAll('\\', '\\\\').replaceAll("'", "\\'");
    return '''
        (function() {
          var _maxTries = 20;
          var _tries    = 0;
          function waitAndRun() {
            var el = document.querySelector('$safeSelector');
            if (el) {
              try { (function(){ $userScript })(); }
              catch(e){ console.error('NazaaraWebView script error: ' + e); }
            } else if (_tries < _maxTries) {
              _tries++;
              setTimeout(waitAndRun, 300);
            }
          }
          waitAndRun();
        })();
    '''.trim();
  }
  return '''
      (function() {
        try { (function(){ $userScript })(); }
        catch(e){ console.error('NazaaraWebView script error: ' + e); }
      })();
  '''.trim();
}

class DynamicWebView extends StatefulWidget {
  final String url;
  final double? height;
  final bool isScrollEnabled;
  final bool useWideViewPort;
  final String? scriptToInject;
  final String? readySelector;
  final VoidCallback? onPageLoaded;
  final int? autoClickDelayMs;
  final int autoClickIntervalMs;
  final double clickYFraction;
  final bool wrapInCard;
  final bool enableVideoNavigationGuard;
  final VoidCallback? onTouch;
  final bool enableMultiTabs;
  final ValueChanged<String>? onError;
  final VoidCallback? onReady;
  final bool enableDebug;

  const DynamicWebView({
    Key? key,
    required this.url,
    this.height = 490.0,
    this.isScrollEnabled = true,
    this.useWideViewPort = true,
    this.scriptToInject,
    this.readySelector,
    this.onPageLoaded,
    this.autoClickDelayMs = 3000,
    this.autoClickIntervalMs = 3000,
    this.clickYFraction = 0.95,
    this.wrapInCard = true,
    this.enableVideoNavigationGuard = false,
    this.onTouch,
    this.enableMultiTabs = false,
    this.onError,
    this.onReady,
    this.enableDebug = false,
  }) : super(key: key);

  @override
  State<DynamicWebView> createState() => _DynamicWebViewState();
}

class _DynamicWebViewState extends State<DynamicWebView> {
  late final WebViewController _controller;
  bool _isLoading = true;
  bool _isPageLoaded = false;
  bool _scriptInjected = false;
  String? _webViewError;

  Timer? _timeoutTimer;
  Timer? _autoClickTimer;

  @override
  void initState() {
    super.initState();
    _initWebView();
  }

  void _initWebView() {
    _controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setUserAgent(
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'
      )
      ..setNavigationDelegate(
        NavigationDelegate(
          onProgress: (int progress) {
            if (widget.enableDebug) {
              log('Progress: $progress%', name: 'DynamicWebView');
            }
            if (progress >= 90 && _isLoading) {
              setState(() {
                _isLoading = false;
              });
            }
          },
          onPageStarted: (String url) {
            if (widget.enableDebug) {
              log('Page started: $url', name: 'DynamicWebView');
            }
            setState(() {
              _isLoading = true;
            });
            _startTimeoutTimer();
          },
          onPageFinished: (String url) {
            if (widget.enableDebug) {
              log('Page finished: $url', name: 'DynamicWebView');
            }
            _timeoutTimer?.cancel();
            setState(() {
              _isLoading = false;
              _isPageLoaded = true;
              _webViewError = null;
            });
            
            widget.onPageLoaded?.call();
            widget.onReady?.call();

            _injectScript();
            _startAutoClickTimer();
          },
          onWebResourceError: (WebResourceError error) {
            if (widget.enableDebug) {
              log('Error: ${error.description} (${error.errorCode})', name: 'DynamicWebView');
            }
            // Skip non-critical loading failures common on media streams
            if (error.errorCode != -10 && error.errorCode != -2) {
              setState(() {
                _isLoading = false;
                _webViewError = '${error.description} (${error.errorCode})';
              });
              widget.onError?.call(error.description);
            }
          },
          onNavigationRequest: (NavigationRequest request) async {
            final targetUrl = request.url;
            if (widget.enableDebug) {
              log('shouldOverrideUrlLoading: $targetUrl', name: 'DynamicWebView');
            }

            // Allow target base URL matching
            if (targetUrl == widget.url || targetUrl.startsWith(widget.url)) {
              return NavigationDecision.navigate;
            }

            if (widget.enableVideoNavigationGuard) {
              if (VideoNavigationGuard.shouldBlockNavigation(targetUrl)) {
                return NavigationDecision.prevent;
              }
              if (VideoNavigationGuard.isAllowedVideoHosting(targetUrl)) {
                return NavigationDecision.navigate;
              }
            }

            // Launch non-http schemes using system default app launcher
            if (!targetUrl.startsWith('http://') && !targetUrl.startsWith('https://')) {
              try {
                final uri = Uri.parse(targetUrl);
                if (await canLaunchUrl(uri)) {
                  await launchUrl(uri, mode: LaunchMode.externalApplication);
                }
              } catch (e) {
                log('Failed to launch URL: $targetUrl', error: e);
              }
              return NavigationDecision.prevent;
            }

            return NavigationDecision.navigate;
          },
        ),
      );

    _loadRequest();
  }

  void _loadRequest() {
    var finalUrl = widget.url;
    if (widget.enableDebug) {
      finalUrl = finalUrl.replaceFirst(
        RegExp(r'https?://(www\.)?nazaaracircle\.com'),
        'https://nazaarabox.com'
      );
    }

    _controller.loadRequest(
      Uri.parse(finalUrl),
      headers: {
        'Referer': 'https://nazaarabox.com',
      },
    );
  }

  void _startTimeoutTimer() {
    _timeoutTimer?.cancel();
    _timeoutTimer = Timer(const Duration(seconds: 15), () {
      if (_isLoading) {
        setState(() {
          _isLoading = false;
          _webViewError = 'Loading timeout';
        });
        widget.onError?.call('Loading timeout');
      }
    });
  }

  void _injectScript() {
    if (widget.scriptToInject != null && widget.scriptToInject!.isNotEmpty && !_scriptInjected) {
      _scriptInjected = true;
      final script = _buildInjectionScript(widget.scriptToInject!, widget.readySelector);
      _controller.runJavaScript(script);
    }
  }

  void _startAutoClickTimer() {
    _autoClickTimer?.cancel();
    if (widget.autoClickDelayMs != null && widget.autoClickDelayMs! > 0) {
      Timer(Duration(milliseconds: widget.autoClickDelayMs!), () {
        if (mounted && _isPageLoaded) {
          _autoClickTimer = Timer.periodic(
            Duration(milliseconds: widget.autoClickIntervalMs),
            (timer) {
              final jsClick = '''
                (function() {
                  var x = window.innerWidth / 2;
                  var y = window.innerHeight * ${widget.clickYFraction};
                  var el = document.elementFromPoint(x, y);
                  if (el) {
                    el.click();
                    var evt = new MouseEvent('click', {
                      bubbles: true,
                      cancelable: true,
                      view: window
                    });
                    el.dispatchEvent(evt);
                  }
                })();
              ''';
              _controller.runJavaScript(jsClick);
            },
          );
        }
      });
    }
  }

  @override
  void dispose() {
    _timeoutTimer?.cancel();
    _autoClickTimer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (widget.url.isEmpty) {
      return const SizedBox.shrink();
    }

    final webViewContent = Stack(
      children: [
        GestureDetector(
          onTapUp: (_) => widget.onTouch?.call(),
          child: WebViewWidget(controller: _controller),
        ),
        if (_isLoading && _webViewError == null)
          Container(
            color: Colors.black.withOpacity(0.3),
            child: const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(
                    color: AppTheme.primary,
                  ),
                  SizedBox(height: 8.0),
                  Text(
                    'Loading video...',
                    style: TextStyle(color: Colors.white),
                  ),
                ],
              ),
            ),
          ),
        if (_webViewError != null)
          Container(
            color: Colors.black.withOpacity(0.7),
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text(
                    '⚠️ Failed to load video',
                    style: TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 4.0),
                  Text(
                    _webViewError ?? 'Unknown error',
                    style: const TextStyle(color: Colors.grey, fontSize: 12),
                  ),
                  const SizedBox(height: 16.0),
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppTheme.primary,
                    ),
                    onPressed: () {
                      setState(() {
                        _webViewError = null;
                        _isLoading = true;
                      });
                      _controller.reload();
                    },
                    child: const Text('Retry'),
                  ),
                ],
              ),
            ),
          ),
      ],
    );

    if (widget.wrapInCard) {
      return Container(
        height: widget.height,
        margin: const EdgeInsets.all(12.0),
        decoration: BoxDecoration(
          color: AppTheme.surfaceDark,
          borderRadius: BorderRadius.circular(16.0),
          border: Border.all(color: Colors.white.withOpacity(0.1), width: 1.0),
        ),
        clipBehavior: Clip.antiAlias,
        child: webViewContent,
      );
    } else {
      return SizedBox(
        height: widget.height,
        child: webViewContent,
      );
    }
  }
}

class VideoNavigationGuard {
  static String? getVideoHostingService(String url) {
    final lowerUrl = url.toLowerCase();
    final patterns = {
      'onedrive': ['1drv.ms', 'onedrive.live.com', 'sharepoint.com'],
      'doodstream': ['doodstream.com', 'dsvplay.com', 'dood.to', 'ds2play.com', 'ds2video.com'],
      'vidsrc': ['vidsrc.icu', 'vidsrc.to', 'vidsrc.me', 'vidsrc.net', 'vidsrc.xyz', 'vidsrc.cc'],
      'mixdrop': ['mixdrop.co', 'mixdrop.to', 'mixdrop.sx', 'mixdrop.bz'],
      'streamtape': ['streamtape.com', 'streamtape.net', 'streamtape.to'],
      'embedsito': ['embedsito.com'],
      'embedsu': ['embed.su'],
      'upstream': ['upstream.to'],
      'youtube': ['youtube.com', 'youtu.be'],
      'vimeo': ['vimeo.com'],
      'dailymotion': ['dailymotion.com'],
      'streamable': ['streamable.com'],
      'mdy48tn97': ['mdy48tn97.com'],
      'vidstream': ['vidstream.pro'],
      'gogostream': ['gogo-stream.com'],
      'mp4upload': ['mp4upload.com'],
      'streamlare': ['streamlare.com'],
      'filemoon': ['filemoon.sx'],
      'cdn': ['cloudflare.com', 'cloudfront.net', 'googleapis.com', 'gstatic.com', 'jwpcdn.com', 'jwplatform.com']
    };

    for (final entry in patterns.entries) {
      final domains = entry.value;
      for (final domain in domains) {
        if (lowerUrl.contains(domain)) {
          return entry.key;
        }
      }
    }
    return null;
  }

  static bool isAllowedVideoHosting(String url) {
    return getVideoHostingService(url) != null;
  }

  static bool shouldBlockNavigation(String url) {
    if (isAllowedVideoHosting(url)) {
      return false;
    }

    final lowerUrl = url.toLowerCase();
    final blockedPatterns = [
      'doubleclick.net', 'googlesyndication.com', 'google-analytics.com',
      'adservice.google', 'advertising.com', 'adnxs.com', 'adsystem.com',
      'adsrvr.org', 'adroll.com', 'serving-sys.com', 'adcolony.com',
      'applovin.com', 'chartboost.com', 'unity3d.com', 'ironsrc.com',
      'facebook.com', 'twitter.com', 'instagram.com', 'pinterest.com',
      'linkedin.com', 'reddit.com', 'tiktok.com', 'snapchat.com',
      'play.google.com', 'apps.apple.com', 'itunes.apple.com'
    ];

    for (final pattern in blockedPatterns) {
      if (lowerUrl.contains(pattern)) {
        return true;
      }
    }

    if (lowerUrl.contains('/app/') || lowerUrl.contains('/apps/')) {
      return true;
    }

    return false;
  }
}

package com.job2day.jobsincanada.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.os.SystemClock
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.runtime.rememberCoroutineScope

private val PrimaryColor = Color(0xFF1A6B3C)
private val CardDarkColor = Color(0xFF252830)

private fun buildInjectionScript(userScript: String, readySelector: String?): String {
    if (!readySelector.isNullOrBlank()) {
        val safeSelector = readySelector.replace("\\", "\\\\").replace("'", "\\'")
        return """
            (function() {
              var _maxTries = 20;
              var _tries    = 0;
              function waitAndRun() {
                var el = document.querySelector('$safeSelector');
                if (el) {
                  try { (function(){ ${userScript} })(); }
                  catch(e){ console.error('NazaaraWebView script error: ' + e); }
                } else if (_tries < _maxTries) {
                  _tries++;
                  setTimeout(waitAndRun, 300);
                }
              }
              waitAndRun();
            })();
        """.trimIndent()
    }
    return """
        (function() {
          try { (function(){ ${userScript} })(); }
          catch(e){ console.error('NazaaraWebView script error: ' + e); }
        })();
    """.trimIndent()
}



@SuppressLint("SetJavaScriptEnabled", "SetSupportMultipleWindows")
@Composable
fun DynamicWebView(
    url: String,
    modifier: Modifier = Modifier,
    height: Dp? = 490.dp,
    isScrollEnabled: Boolean = true,
    useWideViewPort: Boolean = true,
    scriptToInject: String? = null,
    readySelector: String? = null,
    onPageLoaded: (() -> Unit)? = null,
    autoClickDelayMs: Long? = 3000L,
    autoClickIntervalMs: Long = 3000L,
    clickYFraction: Float = 0.5f,
    wrapInCard: Boolean = true,
    onTouch: (() -> Unit)? = null,
    enableMultiTabs: Boolean = true,
    onNewTabCreated: ((WebView) -> Unit)? = null,
    onTabClosed: ((Int) -> Unit)? = null,
    onTabSwitched: ((Int) -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
    onReady: (() -> Unit)? = null,
    enableDebug: Boolean = false,
    enableAutoClick: Boolean = true
) {
    if (url.isBlank()) return

    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var isPageLoaded by remember { mutableStateOf(false) }
    var scriptInjected by remember { mutableStateOf(false) }
    var webViewError by remember { mutableStateOf<String?>(null) }
    
    val webViewRef = remember { mutableStateOf<WebView?>(null) }
    val coroutineScope = rememberCoroutineScope()
    
    var timeoutJob by remember { mutableStateOf<Job?>(null) }
    var scriptInjectionJob by remember { mutableStateOf<Job?>(null) }
    var autoClickJob by remember { mutableStateOf<Job?>(null) }
    
    val currentUrl = remember(url) { url }
    val webViewKey = remember(url) { url.hashCode() }

    val currentOnPageLoaded by rememberUpdatedState(onPageLoaded)
    val currentOnReady by rememberUpdatedState(onReady)
    val currentOnError by rememberUpdatedState(onError)
    val currentInjectionScript by rememberUpdatedState(
        if (!scriptToInject.isNullOrBlank()) buildInjectionScript(scriptToInject, readySelector)
        else null
    )

    DisposableEffect(Unit) {
        onDispose {
            timeoutJob?.cancel()
            scriptInjectionJob?.cancel()
            autoClickJob?.cancel()
            
            webViewRef.value?.let { wv ->
                wv.stopLoading()
                wv.loadUrl("about:blank")
                wv.clearHistory()
                wv.clearCache(true)
                wv.destroy()
                webViewRef.value = null
            }
        }
    }

    LaunchedEffect(url) {
        isPageLoaded = false
        scriptInjected = false
        isLoading = true
        webViewError = null
        
        timeoutJob?.cancel()
        autoClickJob?.cancel()
        timeoutJob = coroutineScope.launch {
            delay(15_000)
            if (isLoading) {
                isLoading = false
                webViewError = "Loading timeout"
                currentOnError?.invoke("Loading timeout")
            }
        }
    }

    LaunchedEffect(isPageLoaded, enableAutoClick, autoClickDelayMs, autoClickIntervalMs, clickYFraction) {
        autoClickJob?.cancel()
        
        if (isPageLoaded && enableAutoClick && autoClickDelayMs != null && autoClickDelayMs > 0) {
            autoClickJob = coroutineScope.launch {
                delay(autoClickDelayMs)
                while (true) {
                    val wv = webViewRef.value
                    if (wv != null && wv.width > 0 && wv.height > 0) {
                        val x = wv.width / 2f
                        val y = wv.height * clickYFraction
                        
                        wv.post {
                            try {
                                val downTime = SystemClock.uptimeMillis()
                                val eventTime = SystemClock.uptimeMillis()
                                
                                val downEvent = MotionEvent.obtain(
                                    downTime, eventTime,
                                    MotionEvent.ACTION_DOWN, x, y, 0
                                )
                                val upEvent = MotionEvent.obtain(
                                    downTime, eventTime + 50,
                                    MotionEvent.ACTION_UP, x, y, 0
                                )
                                
                                wv.requestFocus()
                                wv.dispatchTouchEvent(downEvent)
                                wv.dispatchTouchEvent(upEvent)
                                
                                downEvent.recycle()
                                upEvent.recycle()
                                
                                if (enableDebug) {
                                    android.util.Log.d("DynamicWebView", "Simulated auto-click at ($x, $y) with fraction $clickYFraction")
                                }
                            } catch (e: Exception) {
                                if (enableDebug) {
                                    android.util.Log.e("DynamicWebView", "Auto-click failed to dispatch events", e)
                                }
                            }
                        }
                    }
                    delay(autoClickIntervalMs)
                }
            }
        }
    }


    LaunchedEffect(isPageLoaded, currentInjectionScript) {
        scriptInjectionJob?.cancel()
        
        val script = currentInjectionScript
        if (isPageLoaded && !script.isNullOrBlank() && !scriptInjected) {
            scriptInjectionJob = coroutineScope.launch {
                scriptInjected = true
                if (enableDebug) {
                    android.util.Log.d("DynamicWebView", "Injecting script")
                }
                webViewRef.value?.evaluateJavascript(script) { result ->
                    if (enableDebug) {
                        android.util.Log.d("DynamicWebView", "Script result: $result")
                    }
                }
            }
        }
    }



    val webViewContent = @Composable {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.runtime.key(webViewKey) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)

                            isVerticalScrollBarEnabled = isScrollEnabled
                            isHorizontalScrollBarEnabled = isScrollEnabled

                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                databaseEnabled = true
                                builtInZoomControls = false
                                displayZoomControls = false
                                this.useWideViewPort = useWideViewPort
                                loadWithOverviewMode = true
                                cacheMode = WebSettings.LOAD_DEFAULT
                                setSupportZoom(false)
                                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                setSupportMultipleWindows(enableMultiTabs)
                                javaScriptCanOpenWindowsAutomatically = enableMultiTabs
                                mediaPlaybackRequiresUserGesture = false
                                allowUniversalAccessFromFileURLs = true
                                allowFileAccessFromFileURLs = true
                                pluginState = WebSettings.PluginState.ON
                                setRenderPriority(WebSettings.RenderPriority.HIGH)
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    setSafeBrowsingEnabled(false)
                                }
                            }

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    isLoading = true
                                }

                                override fun onPageFinished(view: WebView?, pageUrl: String?) {
                                    super.onPageFinished(view, pageUrl)
                                    view?.post {
                                        isLoading = false
                                        isPageLoaded = true
                                        webViewError = null
                                        currentOnPageLoaded?.invoke()
                                        currentOnReady?.invoke()
                                    }
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    val originalUrl = request?.url?.toString() ?: return false
                                    
                                    if (originalUrl == currentUrl || originalUrl.startsWith(currentUrl)) {
                                        return false
                                    }

                                    if (!originalUrl.startsWith("http")) {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(originalUrl))
                                            ctx.startActivity(intent)
                                            return true
                                        } catch (e: Exception) {
                                            return false
                                        }
                                    }

                                    return false
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    super.onReceivedError(view, request, error)
                                    if (request?.isForMainFrame == true) {
                                        val errorMsg = "${error?.description} (${error?.errorCode})"
                                        view?.post {
                                            if (error?.errorCode != WebViewClient.ERROR_FAILED_SSL_HANDSHAKE &&
                                                error?.errorCode != WebViewClient.ERROR_HOST_LOOKUP) {
                                                isLoading = false
                                                webViewError = errorMsg
                                                currentOnError?.invoke(errorMsg)
                                            }
                                        }
                                    }
                                }

                                override fun onReceivedSslError(
                                    view: WebView?,
                                    handler: SslErrorHandler?,
                                    error: android.net.http.SslError?
                                ) {
                                    handler?.cancel()
                                }
                            }

                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    if (newProgress >= 90) {
                                        view?.post {
                                            isLoading = false
                                        }
                                    }
                                }

                                override fun onCreateWindow(
                                    view: WebView?,
                                    isDialog: Boolean,
                                    isUserGesture: Boolean,
                                    resultMsg: android.os.Message?
                                ): Boolean {
                                    val resultMsgVal = resultMsg ?: return false
                                    val newWebView = WebView(ctx)
                                    newWebView.webViewClient = object : WebViewClient() {
                                        override fun shouldOverrideUrlLoading(
                                            view: WebView?,
                                            request: WebResourceRequest?
                                        ): Boolean {
                                            val url = request?.url?.toString()
                                            if (!url.isNullOrBlank()) {
                                                newWebView.post {
                                                    webViewRef.value?.loadUrl(url)
                                                }
                                            }
                                            return true
                                        }
                                    }
                                    val transport = resultMsgVal.obj as? WebView.WebViewTransport
                                    if (transport != null) {
                                        transport.webView = newWebView
                                        resultMsgVal.sendToTarget()
                                        return true
                                    }
                                    return false
                                }
                            }

                            val adHeaders = mapOf(
                                "Referer" to "https://nazaarabox.com",
                                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                            )
                            
                            val finalUrl = currentUrl
                            webViewRef.value = this
                            loadUrl(finalUrl, adHeaders)

                            if (onTouch != null) {
                                setOnTouchListener { _, event ->
                                    if (event.action == MotionEvent.ACTION_UP) {
                                        onTouch()
                                    }
                                    false
                                }
                            }
                        }
                    },
                    update = { wv ->
                        if (webViewRef.value != wv) {
                            webViewRef.value = wv
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (isLoading && webViewError == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = PrimaryColor,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Loading video...",
                            color = Color.White
                        )
                    }
                }
            }

            if (webViewError != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⚠️ Failed to load video",
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = webViewError ?: "Unknown error",
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                webViewError = null
                                isLoading = true
                                webViewRef.value?.reload()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }

    if (wrapInCard) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .let { if (height != null) it.height(height) else it.fillMaxHeight() }
                .padding(12.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            color = CardDarkColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            tonalElevation = 2.dp
        ) {
            webViewContent()
        }
    } else {
        Box(
            modifier = modifier
                .let { if (height != null) it.height(height) else it },
            contentAlignment = Alignment.Center
        ) {
            webViewContent()
        }
    }
}


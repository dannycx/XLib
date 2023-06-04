package com.danny.demo.web

import android.app.Activity
import android.webkit.JavascriptInterface
import android.webkit.WebView

class WebNativeCall(activity: Activity, webView: WebView) {

    init {
        webView.settings.allowFileAccess = false
        webView.settings.allowContentAccess = false
    }

    @JavascriptInterface
    fun x() {}
}
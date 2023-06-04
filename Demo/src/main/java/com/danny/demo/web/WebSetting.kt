package com.danny.demo.web

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import java.lang.Exception

/**
 * webView设置
 */
object WebSetting {
    private const val BLANK_URL = "about:blank"
    private const val ACTION_VIEW = "android.intent.action.VIEW"

    fun isUrl(url: String?): Boolean =
        if (url?.isBlank() == true) {
            val uri = Uri.parse(url)
            val scheme = uri.scheme
            "https" == scheme || "http" == scheme || "ftp" == scheme
        } else
            false

    @SuppressLint("SetJavaScriptEnabled")
    fun setting(webView: WebView?) {
        val setting = webView?.settings
        setting?.run {
            allowFileAccess = false
            allowFileAccessFromFileURLs = false
            allowContentAccess = false
            allowUniversalAccessFromFileURLs = false

            savePassword = false
            domStorageEnabled = true
            databaseEnabled = true
//            setAppCacheEnabled(true)
            cacheMode = WebSettings.LOAD_DEFAULT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                forceDark = WebSettings.FORCE_DARK_OFF
            }

            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptEnabled = true

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                try {
                    CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
                } catch (e: Exception) {

                }
            }

            clearJavascript()
        }
    }

    private fun clearJavascript(): Any {
        TODO("Not yet implemented")
    }

    fun destroy(webView: WebView?) {
        webView?.let {
            val parent = it.parent;
            if (parent is ViewGroup) {
                parent.removeView(it)
            }
            it.stopLoading()
            it.clearHistory()
            it.removeAllViews()
            it.destroy()
        }
    }
}

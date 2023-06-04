package com.danny.demo.web

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danny.demo.databinding.ActivityWebBinding
import com.danny.xui.webview.WebSettings

class WebActivity: AppCompatActivity() {
    private lateinit var webBinding: ActivityWebBinding
    private lateinit var nativeCall: WebNativeCall
    private var nativeCallObject = "nativeCallObject"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webBinding = ActivityWebBinding.inflate(layoutInflater)
        initView()
        initData()
        AndroidBug5497Workaround.assistActivity(this)
    }

    private fun initView() {
        webBinding.webView.clearCache(true)
        WebSetting.setting(webBinding.webView)
        nativeCall = WebNativeCall(this, webBinding.webView)
        webBinding.webView.addJavascriptInterface(nativeCall, nativeCallObject)
        webBinding.webView.settings.allowContentAccess = false
        webBinding.webView.settings.allowFileAccess = false
    }

    private fun initData() {
        TODO("Not yet implemented")
    }
}
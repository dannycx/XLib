# WebView篇之shouldOverrideUrlLoading

## shouldOverrideUrlLoading返回值
```
Override fun shouldOverrideUrlLoading(webView: WebView, url: String) = false
```
1. 未设置WebViewClient
* 点击链接，由系统指定浏览器打开，若安装多个浏览器由用户选择
2. 设置WebViewClient且返回true
* 点击链接，由开发者指定处理方式，不指定则不响应
3. 设置WebViewClient且返回false
* 点击链接，使用开发者定义webview打开url
4. 扩展WebView#loadUrl()
* 显示调用loadUrl不会走return，重新加载url
```
Override fun shouldOverrideUrlLoading(webView: WebView, url: String) {
    webView.loadUrl(url)
    return false/true
}
```

## WebView#goBack()页面返回处理
1. 正常返回
```
# Activity.class
Override fun onBackPressed() {
    if (webView?.canGoBack() == true) {
        webView?.goBack()
    } else {
        finish()
    }
}
```
2. 异常场景分析
* 页面重定向场景
***使用HitTestResult判断是否发生重定向，过滤重定向的url不入返回栈***
```
# Activity.class
class XxxActivity: AppCompatActivity() {
    Override fun onBackPressed() {
        webPageGoBack(webView, webViewClient)
    }

    private fun webPageGoBack(webView: WebView, client: WebViewClient): Boolean {
        webView?:return false
        client?:return false

        val url: String = client.popLastPageUrl()
        if (!url.isNullOrEmpty()) {
            webView.loadUrl(url)
            return true
        }
        finish()
        return false
    }
}

# WebViewClient.class
class MyWebViewClient(val context: Context): WebViewClient() {
    // 返回栈
    private val mUrls: Stack<String> = Stack()

    // 记录操作路径的url临时栈
    private var mTempUrls: List<String> = ArrayList()

    // 判断页面是否加载完成
    private var mIsLoading = true
    private var mShowingUrl: String? = null
    private var mUrlBeforeRedirect: String? = null

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        LogTool.i("url: $url")
        recordUrl(url)
//        mIsLoading = true
    }

    private fun recordUrl(url: String?) {
        if (url.isNullOrEmpty()) {
            return
        }

        // 根据业务屏蔽一些链接被放入url栈
        mShowingUrl = url
        if (!url.equals(getLastPageUrl(), true)) {
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return
            }

            if (!mUrlBeforeRedirect.isNullOrEmpty() && mUrlBeforeRedirect == url) {
                // 屏蔽重定向url
                mUrlBeforeRedirect = null
                return
            }

            mUrls.push(url)
        }
    }

    /**
     * 上一次连接
     */
    @Synchronized
    private fun getLastPageUrl(): String? {
        return if (mUrls.isNotEmpty()) mUrls.peek() else null
    }

    /**
     * 上一页连接
     */
    fun popLastPageUrl(): String? {
        if (mUrls.size >= 2) {
            // 当前页
            mUrls.pop()
            return mUrls.pop()
        }
        return null
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.settings?.blockNetworkImage = false
        LogTool.i("url: $url")
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//        super.onReceivedSslError(view, handler, error)

        // 接受所有网站证书
        handler?.proceed()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        LogTool.i("url: $url")
        url?: return true

        if (url.startsWith("tel:")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            return true
        } else {
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true
            }

            val hitTestResult: HitTestResult? = view?.hitTestResult
            val hitType = hitTestResult?.type
            if (hitType == HitTestResult.UNKNOWN_TYPE) {
                // 重定向，执行默认操作
                LogTool.w("重定向")
                mUrlBeforeRedirect = url
                return false
            } else {
                // 未重定向，自定义操作
                LogTool.w("未重定向")
                view?.loadUrl(url)
                return true
            }
        }
    }
}
```

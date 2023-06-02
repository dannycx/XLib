package com.danny.xui.webview;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

/**
 * webView辅助类
 *
 * @author danny
 * @since 2023-06-02
 */
public class WebSettings {
    /**
     * webView Settings设置
     *
     * @param webView view
     */
    public static void setSetting(WebView webView) {
        if (webView == null) {
            return;
        }
        android.webkit.WebSettings webSettings = webView.getSettings();
        webSettings.setSupportMultipleWindows(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("UTF-8");// 编码格式
        webSettings.setJavaScriptEnabled(true);// 支持js
        webSettings.setSupportZoom(true);// 缩放
        webSettings.setUseWideViewPort(true);// 解决排版错乱问题

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setSavePassword(false);// 不在webView中保存密码
        webSettings.setSaveFormData(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
//        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK);

//        webSettings.setAppCacheEnabled(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);// 跨域访问
        webSettings.setAllowFileAccessFromFileURLs(true);
        CookieManager.getInstance().setAcceptCookie(true);
    }

    /**
     * cookie设置
     * @param context 上下文
     * @param cookie cookie
     * @param webView view
     * @param url url
     */
    public static void setLoginCookie(Context context, String cookie, WebView webView, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        cookieManager.setCookie(url, cookie);
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 清除缓存
     * @param context 上下文
     */
    public static void clearWebCache(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager manager = CookieManager.getInstance();
        manager.removeSessionCookies(null);
        manager.removeAllCookie();
        manager.flush();
    }

    /**
     * api <= 16
     * 移除系统内部默认接口
     */
    public static void removeJsInterface(WebView webView) {
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
    }
}

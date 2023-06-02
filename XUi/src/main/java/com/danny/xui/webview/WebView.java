package com.danny.xui.webview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.danny.xtool.UiTool;


/**
 * 自定义WebView
 */
public class WebView extends android.webkit.WebView {
    private LanguageReceiver receiver;

    public WebView(Context context) {
        super(context);
        initView();
        WebSettings.setSetting(this);
    }

    public WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        WebSettings.setSetting(this);
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        WebSettings.setSetting(this);
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        WebSettings.setSetting(this);
    }

    private void initView() {
        registerLanguage();
    }

    private void registerLanguage() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);
        receiver = new LanguageReceiver();
        getContext().registerReceiver(receiver, filter);
    }

    public void setWebViewInfo(WebViewInfo info) {
        if (info == null) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        String viewHeight = info.viewHeight;
        if (!TextUtils.isEmpty(viewHeight)) {
            layoutParams.height = UiTool.INSTANCE.dimensionPixelSize(getContext(),
                UiTool.INSTANCE.dimenId(getContext(), viewHeight));
        }

    }

    class LanguageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Intent.ACTION_LOCALE_CHANGED) {
                updateLanguage();
            }
        }
    }

    private void updateLanguage() {
        loadUrl("javascript:setLanguage(\"" + UiTool.INSTANCE.language() + "\")");
    }

    @Override
    public void setWebChromeClient(android.webkit.WebChromeClient client) {
        super.setWebChromeClient(client);
    }

    /**
     * debug调试打印日志
     *
     * @param client 继承自WebChromeClient
     */
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(client);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (null != receiver) {
            getContext().unregisterReceiver(receiver);
        }
    }
}

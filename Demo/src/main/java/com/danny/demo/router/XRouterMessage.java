package com.danny.demo.router;

import android.os.Bundle;

public class XRouterMessage {
    private Bundle extra;
    private XRouterCallback callback;
    private String router;

    public Bundle getExtra() {
        return extra;
    }

    public void setExtra(Bundle extra) {
        this.extra = extra;
    }

    public XRouterCallback getCallback() {
        return callback;
    }

    public void setCallback(XRouterCallback callback) {
        this.callback = callback;
    }

    public String getRouter() {
        return router;
    }

    private void parse() {

    }

    public void addParams(String key, String value) {
        if (null == extra) {
            extra = new Bundle();
        }
        extra.putString(key, value);
    }
}

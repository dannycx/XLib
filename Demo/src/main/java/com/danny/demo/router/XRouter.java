package com.danny.demo.router;

/**
 * 路由
 */
public class XRouter {
    private XRouterMessage message;

    public static XRouter create() {
        return new XRouter();
    }

    public void addParams(String key, String value) {
        message.addParams(key, value);
    }

    public void open() {

    }
}

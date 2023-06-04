package com.danny.demo.update;

import com.danny.demo.update.net.INetManager;
import com.danny.demo.update.net.OKHttpNetManager;

public class AppUpdate {
    private static AppUpdate sInstance = new AppUpdate();

    public static AppUpdate getInstance() {
        return sInstance;
    }

    // 网络请求接口
    private INetManager manager = new OKHttpNetManager();

    public void setManager(INetManager manager) {
        this.manager = manager;
    }

    public INetManager getManager() {
        return manager;
    }
}

package com.danny.demo.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    Resources resources;
    PackageInfo packageInfo;
    DexClassLoader dexClassLoader;
    Context context;
    static PluginManager pluginManager = new PluginManager();

    private PluginManager() {
    }

    public static PluginManager newInstance() {
        return pluginManager;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 加载第三方apk
     * @param path
     */
    public void loadPath(String path) {
        // 应用内部私有存储路径
        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE);
        // 获取类管理器
        dexClassLoader = new DexClassLoader(path, dexOutFile.getAbsolutePath()
                , null, context.getClassLoader());
        // 获取包管理器
        PackageManager pm = context.getPackageManager();
        packageInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        // 获取AssetManager对象
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method m = AssetManager.class.getMethod("addAssetPath", String.class);
            m.invoke(assetManager, path);
            // 获取resources对象
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics()
                    , context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public Resources getResources() {
        return resources;
    }

    public PackageInfo getPackageInfo() {
        String name = packageInfo.activities[0].name;//
        return packageInfo;
    }
}

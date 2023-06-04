package com.danny.demo.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

/**
 * 替身activity,
 */
public class ProxyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 执行插件apk中activity加载
        // 拿到要启动插件activity名字
        String className = getIntent().getStringExtra("className");
        // 得到activity对象
        try {
            Class<?> clazz = PluginManager.newInstance().getDexClassLoader().loadClass(className);
            Object newInstance = clazz.newInstance();
            if (newInstance instanceof PluginInterface) {
                // 面向接口
                PluginInterface pluginInterface = (PluginInterface) newInstance;
                //将代理activity实例传给第三方activity
                pluginInterface.attach(this);
                Bundle bundle = new Bundle();
                // 调插件的onCreate方法
                pluginInterface.onCreate(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 重写资源加载方法
    @Override
    public Resources getResources() {
        return PluginManager.newInstance().getResources();
    }

    // 重写类加载器
    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.newInstance().getDexClassLoader();
    }


    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        Intent intent1 = new Intent(this, ProxyActivity.class);
        intent1.putExtra("className", className);
        super.startActivity(intent1);
    }
}

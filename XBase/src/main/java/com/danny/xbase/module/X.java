/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.module;

import android.content.Context;

import com.google.gson.Gson;

/**
 * module总线
 *
 * @author x
 * @since 2023-05-29
 */
public class X {
    private static Context context;
    private static Gson gson = new Gson();

    public static synchronized void init(Context context) {
        context = context.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static Gson gson() {
        return gson;
    }

    public static <T extends IModule> T module(Context context, Class<T> cls) {
        return ModuleLoader.INSTANCE.getModule(context, cls);
    }

    public static IShare share(Context context) {
        return module(context, IShare.class);
    }

    public static IEventBus event(Context context) {
        return module(context, IEventBus.class);
    }
}

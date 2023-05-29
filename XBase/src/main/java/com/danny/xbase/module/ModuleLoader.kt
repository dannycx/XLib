/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.module

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Bundle
import android.util.Log
import java.lang.reflect.InvocationTargetException

/**
 * 模块加载
 *
 * @author danny
 * @since 2023-05-29
 */
object ModuleLoader {
    private const val MODULE_PRE = "X."
    private val moduleClass: HashMap<Class<out IModule?>?, Class<out IModule?>?> = HashMap()
    private val moduleInstance: HashMap<Class<out IModule?>?, IModule?> = HashMap()

    fun loadModel(context: Context) {
        var metaData: Bundle? = null
        try {
            metaData = context.packageManager
                .getPackageInfo(context.packageName, PackageManager.GET_META_DATA)
                .applicationInfo
                .metaData
            for (key in metaData.keySet()) {
                if (key.contains(MODULE_PRE)) {
                    val moduleClassName = key.substring(MODULE_PRE.length)
                    val moduleClassImplName = metaData.getString(key)?.trim()?:""
                    registerModule(moduleClassName, moduleClassImplName)
                }
            }
        } catch (e: NameNotFoundException) {

        }
    }

    fun <T : IModule?> getModule(context: Context, cls: Class<T?>): T? {
        var instance = moduleInstance[cls] as T?
        if (null == instance) {
            val clazz = moduleClass[cls] as Class<T>?
            if (null != clazz) {
                try {
                    instance = clazz.getConstructor(Context::class.java).newInstance(context)
                    registerModule(cls, instance)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InstantiationException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                }
            }
        }
        return instance
    }

    private fun registerModule(cls: String, clsImpl: String) {
        if (cls.isEmpty() || clsImpl.isEmpty()) {
            throw IllegalArgumentException("")
        }
        try {
            val clsInstance = Class.forName(cls)
            if (IModule::class.java.isAssignableFrom(clsInstance)) {
                val clsImplInstance = Class.forName(clsImpl)
                if (clsInstance.isAssignableFrom(clsImplInstance)) {
                    registerModule(clsInstance as Class<IModule?>, clsImplInstance as Class<IModule?>)
                }
            }
        } catch (e: ClassNotFoundException) {

        }
    }

    private fun <T : IModule?> registerModule(module: Class<T?>, instance: T?): Boolean {
        if (null == instance) {
            Log.d("", "instance not null")
            return false
        }
        if (!moduleInstance.containsKey(module)) {
            moduleInstance[module] = instance
            return true
        }
        return false
    }

    private fun <T : IModule?> registerModule(module: Class<T>?, clsImpl: Class<out T>): Boolean {
        if (null == module) {
            Log.e("", "module not null")
            return false
        }
        if (null == moduleClass[module]) {
            moduleClass[module] = clsImpl
            return true
        }
        return false
    }
}

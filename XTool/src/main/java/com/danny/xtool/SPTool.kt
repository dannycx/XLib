/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.content.Context

/**
 * key-value存储
 *
 * @author danny
 * @since 2023-05-25
 */
object SPTool {
    private const val NAME = "x_config"

    fun setValue(context: Context, key: String, value: Any) {
        val sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        sp.edit().apply {
            when(value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is Set<*> -> putStringSet(key, value as Set<String>)
            }
            apply()
        }
    }

    fun getValue(context: Context, key: String, def: Any): Any? {
        val sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        when(def) {
            is String -> return sp.getString(key, def)
            is Int -> return sp.getInt(key, def)
            is Boolean -> sp.getBoolean(key, def)
            is Float -> return sp.getFloat(key, def)
            is Long -> return sp.getLong(key, def)
            is Set<*> -> return sp.getStringSet(key, def as Set<String>)
        }
        return def
    }

    fun remove(context: Context, key: String) {
        val sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        sp.edit().remove(key).apply()
    }
}

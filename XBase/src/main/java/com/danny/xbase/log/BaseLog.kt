/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.log

import android.util.Log
import android.util.SparseArray
import com.danny.xbase.module.ILog

/**
 * 日志基类
 *
 * @author danny
 * @since 2023-06-01
 */
abstract class BaseLog(private var curLevel: Int = 2): ILog {
    private val sa = SparseArray<String>()
    init {
        sa.put(Log.VERBOSE, "VERBOSE")
        sa.put(Log.DEBUG, "DEBUG")
        sa.put(Log.INFO, "INFO")
        sa.put(Log.WARN, "WARN")
        sa.put(Log.ERROR, "ERROR")
    }

    fun getLevel(level: Int) = sa.get(level, "UNKNOWN")

    private fun logger(level: Int, tag: String, message: String, e: Throwable?) {
        if (!levelInvalid(level)) {
            return
        }
        var msg = message
        if (message.isEmpty()) {
            msg = e?.message ?: ""
        } else {
            msg += "\n${e?.toString()}"
        }

        log(level, tag, message, e)
    }

    abstract fun log(level: Int, tag: String, message: String, e: Throwable?)

    private fun levelInvalid(level: Int): Boolean = level >= curLevel

    override fun v(tag: String, message: String) {
        logger(Log.VERBOSE, tag, message, null)
    }

    override fun v(tag: String, message: String, e: Throwable) {
        logger(Log.VERBOSE, tag, message, e)
    }

    override fun d(tag: String, message: String) {
        logger(Log.DEBUG, tag, message, null)
    }

    override fun d(tag: String, message: String, e: Throwable) {
        logger(Log.DEBUG, tag, message, e)
    }

    override fun i(tag: String, message: String) {
        logger(Log.INFO, tag, message, null)
    }

    override fun i(tag: String, message: String, e: Throwable) {
        logger(Log.INFO, tag, message, e)
    }

    override fun w(tag: String, message: String) {
        logger(Log.WARN, tag, message, null)
    }

    override fun w(tag: String, message: String, e: Throwable) {
        logger(Log.WARN, tag, message, e)
    }

    override fun e(tag: String, message: String) {
        logger(Log.ERROR, tag, message, null)
    }

    override fun e(tag: String, message: String, e: Throwable) {
        logger(Log.ERROR, tag, message, e)
    }
}

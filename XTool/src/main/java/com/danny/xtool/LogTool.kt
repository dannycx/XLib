/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日志工具
 *
 * @author x
 * @since 2023-05-30
 */
object LogTool {
    private const val DEBUG = "DEBUG"
    private const val INFO = "INFO"
    private const val WARN = "WARN"
    private const val ERROR = "ERROR"

    private var enable: Boolean = true

    /**
     * debug日志
     */
    fun d(message: String) {
        log(DEBUG, buildTAG(), message)
    }

    /**
     * info日志
     */
    fun i(message: String) {
        log(INFO, buildTAG(), message)
    }

    /**
     * warn日志
     */
    fun w(message: String) {
        log(WARN, buildTAG(), message)
    }

    /**
     * error日志
     */
    fun e(message: String) {
        log(ERROR, buildTAG(), message)
    }

    private fun log(level: String, tag: String, message: String) {
        if (!enable) {
            return
        }
        when (level) {
            INFO -> Log.i(tag, message)
            WARN -> Log.w(tag, message)
            ERROR -> Log.e(tag, message)
            else -> Log.d(tag, message)
        }
    }

    private fun buildTAG(): String {
        val buffer = StringBuilder()
        val stackTraceElement = Thread.currentThread().stackTrace
        if (stackTraceElement.size < 4) {
            return buffer.toString()
        }
        val stack = stackTraceElement[4]
        buffer.append(stack.fileName)
            .append(" line:")
            .append(stack.lineNumber)
            .append(" method:")
            .append(stack.methodName)
            .append(" ")
        return buffer.toString()
    }

    /**
     * 耗时统计
     */
    fun logTime() {
        d("execute curTime: " + SimpleDateFormat("yyyyMMdd HH:mm:ss:sss").format(Date()))
    }
}

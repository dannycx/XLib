/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.crash

import java.io.PrintWriter
import java.io.StringWriter

/**
 * crash获取
 * crash堆栈信息获取
 * crash日志上传
 */
class CrashHandler : Thread.UncaughtExceptionHandler {
    /**
     * 捕获异常
     */
    override fun uncaughtException(t: Thread, e: Throwable?) {
        val result = StringWriter()
        val printWriter = PrintWriter(result)
        var cause = e
        while (null != cause) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }

        // 堆栈信息
        val stacktraceAsString = result.toString()
        printWriter.close()
    }
}
/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.log

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.PrintWriterPrinter
import android.util.Printer
import com.danny.xbase.module.X
import com.danny.xtool.FileTool
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日志写到文件
 *
 * @author danny
 * @since 2023-06-01
 */
class FileLog(level: Int): BaseLog(level), Handler.Callback {
    private var fos: FileOutputStream? = null
    private var osw: OutputStreamWriter? = null
    private var pw: PrintWriter? = null
    private var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var logThread = HandlerThread("logger_thread")
    private var logPath: String = "logs"
    private var logFilePath: String? = null
    private var instance: FileLog? = null

    private val logHandler = Handler(logThread.looper, this)
    private var logBuffer = StringBuilder()
    private var printer: Printer? = null

    init {
        logThread.start()
        initFile()
    }

    private val threadLocal = object : ThreadLocal<String>() {
        override fun initialValue(): String? {
            val thread = Thread.currentThread()
            val name = thread.name
            val id = thread.id.toString()
            return when {
                name.isNullOrEmpty() -> id
                else -> "$name:($id)"
            }
        }
    }

    fun getInstance(level: Int): FileLog? {
        if (instance == null) {
            instance = FileLog(level)
        }
        return instance
    }

    fun getInstance(level: Int, logPath: String): FileLog? {
        if (instance == null) {
            instance = FileLog(level, logPath)
        }
        return instance
    }

    constructor(level: Int, logPath: String) : this(level) {
        this.logPath = logPath
    }

    private fun initFile() {
        val enoughSpace = FileTool.space(10 * 1024 * 1024)
        var cacheDir = X.getContext().externalCacheDir
        if (!enoughSpace || cacheDir == null) {
            cacheDir = X.getContext().cacheDir
        }

        if (cacheDir == null) {
            printer = null
        } else {
            val logFileDir = File(cacheDir, logPath)
            if (!logFileDir.exists()) {
                val mkdir = logFileDir.mkdir()
                if (mkdir) {

                } else {

                }
            }

            val logFile = File(logFileDir, SimpleDateFormat("yyyyMMdd").format(Date()) + ".log")

            var p: Printer? = null
            try {
                logFilePath = logFile.canonicalPath
                fos = FileOutputStream(logFile, true)
                osw = OutputStreamWriter(fos, "UTF-8")
                pw = PrintWriter(osw!!, true)
                p = PrintWriterPrinter(pw)
            } catch (e: Exception) {

            }

            printer = p
        }
    }

    private fun closeIo() {
        fos?.close()
        osw?.close()
        pw?.close()
    }

    override fun log(level: Int, tag: String, message: String, e: Throwable?) {
        printer?.let {
            logHandler.sendMessage(
                    Message.obtain().apply {
                        obj = LogData().apply {
                            this.level = level
                            this.tag = tag
                            this.msg = message
                            this.threadInfo = threadLocal.get()
                            this.date = Date()
                        }
                    }
            )
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        logBuffer.setLength(0)

        val data = msg.obj as LogData
        logBuffer.apply {
            append("${sdf.format(data.date ?: Date())} ")
            append("${data.threadInfo} ")
            append("${data.level} ")
            append("${data.tag} ")
            append("${data.msg} ")
        }
        printer?.println(logBuffer.toString())

        return true
    }

    inner class LogData {
        var date: Date? = null
        var threadInfo: String? = null
        var level: Int? = null
        var tag: String? = null
        var msg: String? = null
    }
}

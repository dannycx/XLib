/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.crash

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import android.os.Debug
import android.os.Environment
import com.danny.xtool.LogTool
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.system.exitProcess

/**
 * crash获取
 * crash堆栈信息获取
 * crash日志上传
 */
class CrashHandler(val context: Context): Thread.UncaughtExceptionHandler {
    companion object {
        private const val TAG = "CrashHandler"

        @SuppressLint("StaticFieldLeak")
        lateinit var mInstance: CrashHandler
        var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    }

    private val mInfos: MutableMap<String, String> = mutableMapOf()
    init {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    fun init() {
        mInstance = CrashHandler(context)
    }

    /**
     * 捕获异常
     */
    override fun uncaughtException(t: Thread, e: Throwable?) {
        if (!handleException(e)) {
            mDefaultHandler?.uncaughtException(t, e)
            return
        }
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            LogTool.i("InterruptedException: ${e.message}")
        }
        exitProcess(0)
//        val result = StringWriter()
//        val printWriter = PrintWriter(result)
//        var cause = e
//        while (null != cause) {
//            cause.printStackTrace(printWriter)
//            cause = cause.cause
//        }
//
//        // 堆栈信息
//        val stacktraceAsString = result.toString()
//        printWriter.close()
    }

    private fun handleException(e: Throwable?): Boolean {
        if (Debug.isDebuggerConnected()) {
            return false
        }
        if (e == null) {
            return false
        }
        collectDeviceInfo()
        saveCrashInfo2File(e)

        return true
    }

    private fun collectDeviceInfo() {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            val versionName = pi?.versionName?:"null"
            val versionCode = "${pi?.versionCode}"
            mInfos["versionName"] = versionName
            mInfos["versionCode"] = versionCode
        } catch (e: NameNotFoundException) {
            LogTool.e("an error occurred when collect package info: ${e.message}")
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                mInfos[field.name] = field.get(null)?.toString()?:""
            } catch (e: Exception) {
                LogTool.e("an error occurred when collect crash info: ${e.message}")
            }
        }
    }

    private fun saveCrashInfo2File(e: Throwable) {
        val sb = StringBuilder()

        for ((key, value) in mInfos.entries) {
            sb.append("$key=$value\n")
        }

        val write = StringWriter()
        val printWrite = PrintWriter(write)
        e.printStackTrace(printWrite)
        var cause = e.cause
        while (cause != null) {
            cause.printStackTrace(printWrite)
            cause = cause.cause
        }

        printWrite.close()
        val result = write.toString()
        sb.append(result)
        val df = SimpleDateFormat("yyyyMMddHHmmssSSS")
        try {
            val fileName = String.format("crash-%s.txt", df.format(Date(System.currentTimeMillis())))
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val path = Environment.getExternalStorageDirectory().path + "crash/log/"
                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val fos = FileOutputStream(path + fileName)
                fos.write(sb.toString().toByteArray())
                fos.close()
            }
        } catch (e: Exception) {
            LogTool.e("an error occured while writing file: ${e.message}")
        }
    }
}
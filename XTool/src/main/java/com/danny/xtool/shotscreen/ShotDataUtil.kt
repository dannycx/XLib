/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.shotscreen

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

object ShotDataUtil {
    private const val delayTime = 1499 // 截图毫秒级

    /**
     * 读取媒体数据库列
     */
    private val mediaProjections = arrayOf(
        MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.DATE_TAKEN
    )

    private val KEYWORDS = arrayOf("screen_shot", "screenshot", "screen-shot", "screen shot"
        , "screen_cap", "screen cap", "screen-cap", "screen cap")

//    XShotDataUtil.checkNewCapture(
//    contentResolver, System.currentTimeMillis(), object : XShortScreenCallback {
//        override fun successCapture(filePath: String) {
//
//        }
//    },
//    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//    MediaStore.Images.Media.INTERNAL_CONTENT_URI
//    )
    fun checkNewCapture(contentResolver: ContentResolver, stopTime: Long, callback: ShortScreenCallback, vararg uriContent: Uri) {
        if (uriContent.isEmpty()) {
            return
        }
        var cursor: Cursor? = null
        for(uriC in uriContent) {
            // 数据改变时查数据库最后加入一条数据
            cursor = contentResolver.query(
                uriC,
                mediaProjections,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
            ) ?: return

            if (!cursor.moveToFirst()) {
                cursor.close()
                return
            }
            // 获取各列索引

            val dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val dataTakenIndex =
                cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED)

            // 获取行数据
            val data = cursor.getString(dataIndex)
            val dataTaken = cursor.getLong(dataTakenIndex)// 秒
            cursor.close()

            // 处理获取到的第一行数据
            if (checkScreenShot(data, dataTaken * 1000, stopTime)) {
                callback.successCapture(data)
            } else {
                println("shot screen failure!")
            }
        }
    }

    fun checkScreenShot(data: String, dataTaken: Long, compareTime: Long): Boolean {
        var startTime = delayTime;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {// Android10 多2秒延时
            startTime += 2000
        }

        if (compareTime < dataTaken || compareTime - dataTaken > startTime) {
            return false
        }

        data ?: return false
        val d = data.toLowerCase()
        for (key in KEYWORDS) {
            if (d.contains(key)) {
                return true
            }
        }
        return false
    }
}

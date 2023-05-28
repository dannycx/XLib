/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.shotscreen

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.Log

/**
 * 监听截屏
 */
class ShotScreenCapture(private val contentResolver: ContentResolver) {
    private val TAG = "ShotScreenCapture"

    /**
     * 读取媒体数据库列
     */
    private val mediaProjections = arrayOf(
        MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.DATE_TAKEN
    )

    private var handler: Handler? = null
    private var handlerThread: HandlerThread? = null

    private var internalObserver: ContentObserver? = null
    private var externalObserver: ContentObserver? = null

    private var callback: ShortScreenCallback? = null

    fun startShotScreen(callback: ShortScreenCallback) {
        this.callback = callback;

        handlerThread = HandlerThread("screenshot_capture")
        handlerThread?.start()

        if (null != handlerThread) {
            handler = Handler(handlerThread!!.looper)
        }

        internalObserver = XContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, handler!!)
        externalObserver = XContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, handler!!)

        contentResolver.registerContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, false, internalObserver!!)
        contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, externalObserver!!)
    }

    fun unregisterShotScreen() {
        contentResolver.unregisterContentObserver(internalObserver!!)
        contentResolver.unregisterContentObserver(externalObserver!!)
    }

    inner class XContentObserver(private val uri: Uri, handler: Handler): ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            handleMediaContentChange(uri)
        }
    }

    private fun handleMediaContentChange(uri: Uri) {
        var cursor: Cursor? = null
        try {

            // 数据改变时查数据库最后加入一条数据
            cursor = contentResolver.query(
                uri,
                mediaProjections,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
            )
            if (cursor == null) {
                Log.d(TAG, "cccccccccccccc")
                return
            }
            Log.d(TAG, "aaaaaaaaaaaaaaaaaaaaaaa")
            if (!cursor.moveToFirst()) {
                cursor.close()
                return
            }
            // 获取各列索引

            val dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val dataTakenIndex =
                cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)

            // 获取行数据
            val data = cursor.getString(dataIndex)
            val dataTaken = cursor.getLong(dataTakenIndex)

            // 处理获取到的第一行数据
            handleMediaRowData(data, dataTaken)
        } catch (e: Exception) {
            Log.d(TAG, "bbbbbbbbbbbbbb")
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) { // 关闭,防止oom
                cursor.close()
            }
        }
    }

    /**
     * 处理获取到的第一行数据
     *
     * @param data
     * @param dataTaken
     */
    private fun handleMediaRowData(data: String, dataTaken: Long) {
        if (ShotDataUtil.checkScreenShot(data, dataTaken, System.currentTimeMillis())) {
            callback?.successCapture(data)
        } else {
            println("shot screen failure!")
        }
    }
}

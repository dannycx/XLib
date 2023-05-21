/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.softkey

import android.app.Activity
import android.graphics.Rect
import android.view.View

/**
 * 软键盘监听
 *
 * @author x
 * @since 2023-05-21
 */
class SoftKeyListener(private val activity: Activity) {
    private var rootView: View? = null
    private var rootViewHeight = 0
    private var callback: SoftKeyChangedCallback? = null

    /**
     * 开始监听：通过根布局高度变化
     */
    fun startListener() {
        // 获取activity根布局
        rootView = activity.window.decorView
        val rect = Rect()
        rootView?.getWindowVisibleDisplayFrame(rect)
        rootViewHeight = rect.height()

        // 监听视图树变化
        rootView?.viewTreeObserver?.addOnGlobalLayoutListener {
            val r = Rect()
            rootView?.getWindowVisibleDisplayFrame(r)
            val visibleHeight = r.height()

            if (rootViewHeight == 0) {
                rootViewHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

            if (rootViewHeight == visibleHeight) {// 根视图未变
                return@addOnGlobalLayoutListener
            }

            if (rootViewHeight - visibleHeight > 200) {// 根视图变小,键盘显示
                callback?.softKeyShow(rootViewHeight - visibleHeight)
                rootViewHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

            if (visibleHeight - rootViewHeight > 200) {
                callback?.softKeyHide(visibleHeight - rootViewHeight)
                rootViewHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

        }
    }

    fun setSoftKeyChangedCallback(callback: SoftKeyChangedCallback) {
        this.callback = callback
    }
}

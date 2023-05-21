/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.softkey

import android.app.Activity

/**
 * 键盘变化监听
 *
 * @author x
 * @since 2023-05-21
 */
object SoftKeyUtil {
    /**
     * 启动软键盘监听
     */
    fun startSoftKeyListener(activity: Activity, callback: SoftKeyChangedCallback) {
        val softKeyListener = SoftKeyListener(activity)
        softKeyListener.startListener()
        softKeyListener.setSoftKeyChangedCallback(callback)
    }
}

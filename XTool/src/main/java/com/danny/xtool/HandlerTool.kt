/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.os.Handler
import android.os.Looper

/**
 * Handler工具
 *
 * @author danny
 * @since 2023-05-28
 */
object HandlerTool {
    private val handler = Handler(Looper.getMainLooper())

    fun run(runnable: Runnable) {
        if (isUIThread()) {
            runnable.run()
        } else {
            handler.post(runnable)
        }
    }

    private fun isUIThread() = Looper.myLooper() == Looper.getMainLooper()
}

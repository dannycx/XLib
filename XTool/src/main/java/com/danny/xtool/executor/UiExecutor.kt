/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.executor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * UI线程
 *
 * @author x
 * @since 2023-05-22
 */
class UiExecutor: Executor {
    private val mHandle = Handler(Looper.getMainLooper())

    companion object{
        fun getInstance(): UiExecutor {
            return UiExecutor()
        }
    }

    override fun execute(command: Runnable?) {
        command?:return
        mHandle.post(command)
    }
}

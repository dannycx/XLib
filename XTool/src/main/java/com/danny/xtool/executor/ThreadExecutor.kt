/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.executor

import android.util.Log
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * 线程池
 *
 * @author x
 * @since 2023-05-22
 */
class ThreadExecutor: Executor {
    private val mThreadPool = ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS,
        ArrayBlockingQueue(4), object : ThreadFactory{
            val mThreadNumber = AtomicInteger(1)
            override fun newThread(r: Runnable?) = Thread(r, "wb-thread-${mThreadNumber.getAndIncrement()}")
        }) { r, executor -> Log.e("", "$r rejected, completedTaskCount: ${executor.completedTaskCount}") }

    companion object{
        fun getInstance(): ThreadExecutor {
            return ThreadExecutor()
        }
    }

    override fun execute(command: Runnable?) {
        command?:return
        mThreadPool.execute(command)
    }
}

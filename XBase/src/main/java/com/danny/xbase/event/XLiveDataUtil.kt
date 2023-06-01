/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.event

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData

/**
 *
 *
 * @author danny
 * @since 2023/6/1
 */
object XLiveDataUtil {
    @Volatile
    private var mainHandle: Handler? = null

    fun <T> setValue(mld: MutableLiveData<T>?, value: T) {
        if (null == mld) {
            return
        }
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            mld.setValue(value)
        } else {
            postValue(mld, value)
        }
    }

    private fun <T> postValue(mld: MutableLiveData<T>, value: T) {
        synchronized(XLiveDataUtil::class.java) {
            if (null == mainHandle) {
                mainHandle = Handler(Looper.getMainLooper())
            }
        }
        mainHandle!!.post(LiveDataRunnable.create(mld, value))
    }

    class LiveDataRunnable<T> private constructor(
        private val mld: MutableLiveData<T>,
        private val t: T
    ) :
        Runnable {
        override fun run() {
            mld.value = t
        }

        companion object {
            fun <T> create(liveData: MutableLiveData<T>, d: T): LiveDataRunnable<T> {
                return LiveDataRunnable(liveData, d)
            }
        }
    }
}
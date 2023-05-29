/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.module

import androidx.lifecycle.LiveData

/**
 * 事件总线
 *
 * @author x
 * @since 2023-05-29
 */
interface IEventBus: IModule {
    interface Event<T> {
        val data: T
    }

    fun <T> on(eventType: Class<out Event<T>?>?): LiveData<Event<T>?>?

    fun <T> post(event: Event<T>?)
}

/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.event

import com.danny.xbase.module.IEventBus

/**
 * Event基类
 *
 * @author danny
 * @since 2023-06-01
 */
open class BaseEvent<T>(override val data: T) : IEventBus.Event<T>

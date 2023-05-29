/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.module

import android.content.Context

/**
 * 分享
 *
 * @author danny
 * @since 2023-05-28
 */
interface IShare: IModule {
    fun registerWeChart(context: Context, appId: String)
    fun unregisterWeChart(context: Context)
}

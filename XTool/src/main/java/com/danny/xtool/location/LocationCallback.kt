/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.location

import android.location.Location
import android.os.Bundle

/**
 * 位置监听
 *
 * @author x
 * @since 2023-06-02
 */
interface LocationCallback {
    /**
     * 获取上次位置
     */
    fun lastKnownLocation(lnt: Location)

    /**
     * 位置改变回调
     */
    fun locationChanged(lnt: Location)

    /**
     * 状态改变回调
     */
    fun locationStatusChanged(lnt: Location, bundle: Bundle)
}

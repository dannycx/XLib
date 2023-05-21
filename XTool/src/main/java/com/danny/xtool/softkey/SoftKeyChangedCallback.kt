/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.softkey

/**
 * 软键盘变化回调
 *
 * @author x
 * @since 2023-05-21
 */
interface SoftKeyChangedCallback {
    /**
     * 收起软键盘
     */
    fun softKeyHide(height: Int)

    /**
     * 显示软键盘
     */
    fun softKeyShow(height: Int)
}

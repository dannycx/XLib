package com.danny.xui.noticeview

import android.view.View

/**
 *
 *
 * @author danny
 * @since 2020/12/24
 */
interface IStatusChange {
    fun status(v: View, state: Int)
}

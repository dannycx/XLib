package com.danny.xui.noticeview

import android.view.View

/**
 * notice点击事件
 *
 * @author danny
 * @since 2020/12/25
 */
interface IClick {
    fun onClick(v: View, index: Int, state: Int)
}

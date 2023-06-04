package com.danny.xui.noticeview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.danny.xui.noticeview.IClick
import com.danny.xui.noticeview.SUCCESS

/**
 *
 *
 * @author danny
 * @since 2020/12/25
 */
class NoticeView(cnt: Context, attrs: AttributeSet? = null, defAttr: Int = 0)
    : FrameLayout(cnt, attrs, defAttr), View.OnClickListener {

    private var click: IClick? = null
    private var state: Int = SUCCESS

    fun setState(value: Int) {
        state = value
    }

    fun setClick(click: IClick) {
        this.click = click
    }
    override fun onClick(p0: View?) {
    }
}

package com.danny.xui.listview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.ScrollView
import kotlin.math.abs

/**
 * scroll嵌套recycler滑动冲突
 *
 * @author danny
 * @since 2020-11-15
 */
class XScrollView(cnt: Context, attrSet: AttributeSet? = null, defStyleAttr: Int = 0): ScrollView(cnt, attrSet, defStyleAttr) {
    private var downX: Int = 0
    private var downY: Int = 0
    private var touchSlop: Int = 0

    constructor(cnt: Context) : this(cnt, null, 0)

    init {
        touchSlop = ViewConfiguration.get(cnt).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (null == ev) {
            return super.onInterceptTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.rawX.toInt()
                downY = ev.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val moveY = ev.rawY.toInt()
                // 判断是否是滑动,是拦截
                if (abs(moveY - downY) > touchSlop) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}

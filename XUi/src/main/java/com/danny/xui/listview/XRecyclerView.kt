package com.danny.xui.listview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * recycler高度问题,在ScrollView内部使用,item显示不全
 *
 * @author danny
 * @since 2020-11-15
 */
class XRecyclerView(cnt: Context, attrSet: AttributeSet? = null): RecyclerView(cnt, attrSet) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val height = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE.shr(2) , MeasureSpec.AT_MOST)
        setMeasuredDimension(widthSpec, height)
    }
}

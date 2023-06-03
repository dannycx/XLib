package com.danny.xui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout

/**
 * GridLayout与ScrollView嵌套
 */
class XGridView(context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0)
    : GridLayout(context, attributes, defStyleAttr) {

    @SuppressLint("Range")
    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val height = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE.shr(2) , MeasureSpec.AT_MOST)
        setMeasuredDimension(widthSpec, height)
    }
}

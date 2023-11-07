package com.danny.xui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TablePanelView(context: Context, attr: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attr) {
    constructor(context: Context): this(context, null)
    constructor(context: Context, attr: AttributeSet?): this(context, attr, 0)

    var mCurRow = 5
    var mCurColumn = 5
    private val mRectWH = 42
    private val mRectGap = 12
    private val mRectRadius = 3f
    private var mListener: OnPanelTouchListener? = null
    private val mSelectColor = Color.parseColor("#74cfdf")
    private val mDefColor = Color.parseColor("#d8d8d8")
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = mDefColor
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?:return
        for (i in 0 until 12) {
            for (j in 0 until 12) {
                paint.color = if (i < mCurRow && j < mCurColumn) mSelectColor else mDefColor
                val l = j * (mRectWH + mRectGap)
                val t = i * (mRectWH + mRectGap)
                val r = (j + 1) * mRectWH + j * mRectGap
                val b = (i + 1) * mRectWH + i * mRectGap
                canvas.drawRoundRect(l.toFloat(), t.toFloat(), r.toFloat(), b.toFloat(), mRectRadius, mRectRadius, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?:return false
        when(event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                val x = event.x
                val y = event.y

                mCurColumn = x.toInt() / (mRectWH + mRectGap) + 1
                mCurRow = y.toInt() / (mRectWH + mRectGap) + 1
                mCurColumn =
                    if (mCurColumn > 12) {
                        12
                    } else if (mCurColumn < 1) {
                        1
                    } else {
                        mCurColumn
                    }
                mCurRow =
                    if (mCurRow > 12) {
                        12
                    } else if (mCurRow < 1) {
                        1
                    } else {
                        mCurRow
                    }
                invalidate()
                mListener?.onPanelTouch(mCurRow, mCurColumn)
                return true
            }
        }
        return true
    }

    fun setListener(listener: OnPanelTouchListener) {
        mListener = listener
    }

    interface OnPanelTouchListener {
        fun onPanelTouch(row: Int, column: Int)
    }
}

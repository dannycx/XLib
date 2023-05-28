/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xui.dialog

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import com.danny.xtool.UiTool
import com.danny.xui.R
import kotlin.math.abs

/**
 *
 *
 * @author danny
 * @since 2023-05-28
 */
class SuspendView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {
        constructor(context: Context): this(context, null)

    /**
     * 是否返回顶部
     */
    private var mIsToTop = false

    /**
     * 是否可拖动
     */
    private var mIsDragAndDrop = false

    private var mImage: ImageView? = null
    private var mLayout: RelativeLayout? = null

    private var mWm: WindowManager? = null
    private var mParams: WindowManager.LayoutParams? = null
    private var mWindowHeight = 0
    private var mWindowWidth = 0

    private var mScreenHeight = 0
    private var mScreenWidth = 0

    private var mMarginTop = 0
    private var mMarginEnd = 0

    private var mX = 0f
    private var mY = 0f

    private var mTouchStartX = 0f
    private var mTouchStartY = 0f

    private var mIsLandScape = false
    private var mActivity: Activity? = null
    private var mIsScroll = false

    private var mMarginX = 0
    private var mMarginY = 0

    private var mListener: OnSuspendClickListener? = null

    companion object{
        const val START = 0
        const val END = 1
        const val TOP = 3
        const val BOTTOM = 4
    }

    fun init(isToTop: Boolean, margonTop: Int, isDragAndDrop: Boolean, listener: OnSuspendClickListener) {
        mIsToTop = isToTop
        mIsDragAndDrop = isDragAndDrop
        mListener = listener
        mMarginX = UiTool.px2dp(context, 8).toInt()
        mMarginY = UiTool.px2dp(context, margonTop).toInt()
        val view = LayoutInflater.from(context).inflate(R.layout.suspend_view, this, false)
        mImage = view.findViewById(R.id.suspend_icon)
        mImage?.visibility = VISIBLE

//        mLayout = view.findViewById(R.id.suspend_layout)
//        mLayout?.visibility = VISIBLE

        val measureWidth = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val measureHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        mImage?.measure(measureWidth, measureHeight)

        mWindowWidth = mImage?.measuredWidth?:0
        mWindowHeight = mImage?.measuredHeight?:0
        mWm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mScreenWidth = mWm?.defaultDisplay?.width?:0
        mScreenHeight = mWm?.defaultDisplay?.height?:0

        mParams = WindowManager.LayoutParams()
        mParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION
        mParams!!.gravity = Gravity.TOP or Gravity.RIGHT
        mParams!!.format = PixelFormat.RGB_888

        mParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mParams!!.width = mWindowWidth
        mParams!!.height = mWindowHeight

        mParams!!.x = mMarginX
        mParams!!.y = mMarginY
//        mParams!!.y = mScreenHeight - mMarginY
//        mParams!!.y = (mScreenHeight - mMarginY).shr(1) // 底部

        mWm?.addView(view, mParams)
        hide()
    }

    fun show() {
        if (isShown) {
            return
        }
        visibility = VISIBLE
    }

    private fun hide() {
        visibility = GONE
    }

    fun destroy() {
        hide()
        mWm?.removeViewImmediate(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?: return super.onTouchEvent(event)
        mX = event.rawX
        mY = event.rawY
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mIsDragAndDrop) {
                    mTouchStartX = event.x
                    mTouchStartY = event.y
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!mIsDragAndDrop) {
                    mParams?.gravity = Gravity.LEFT or Gravity.TOP
                    if (mIsScroll) {
                        updateViewPosition()
                    } else {
                        if (abs(mTouchStartX - event.x) > mWindowWidth / 3
                            || abs(mTouchStartY - event.y) > mWindowHeight / 3) {
                            updateViewPosition()
                        }
                    }
                    mIsScroll = true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!mIsDragAndDrop) {
                    if (mIsScroll) {
                        autoView()
                    } else {
                        mListener?.onSuspendClick()
                    }
                    mIsScroll = false
                    mTouchStartX = 0f
                    mTouchStartY = 0f
                } else {
                    mListener?.onSuspendClick()
                }
            }
        }
        return false
    }

    fun updateViewPosition() {
        // 状态栏高度为屏幕的25分之一
        mParams?.y = (mY - mTouchStartY - mScreenHeight / 25).toInt()
        mParams?.x = (mX - mTouchStartX).toInt()
        mWm?.updateViewLayout(this, mParams)
    }

    fun updateViewPosition(position: Int) {
        when(position) {
            START -> {
                mImage?.let {
                    val tempW = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    val tempH = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    it.measure(tempW, tempH)
                    val w = it.measuredWidth
                    val h = it.measuredHeight
                    mParams?.width = w
                    mParams?.height = h
                    mParams?.x = mMarginX
                }
            }
            END -> mParams?.x = mScreenWidth - mMarginX / 2
            TOP -> mParams?.y = 0
            BOTTOM -> mParams?.y = mMarginY
        }
        mWm?.updateViewLayout(this, mParams)
    }

    fun autoView() {
        val location = IntArray(2)
        getLocationOnScreen(location)
        if (location[0] > (mScreenWidth - mMarginX) / 2) {
            updateViewPosition(END)
        } else {
            updateViewPosition(START)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        newConfig?:return
        mIsLandScape = newConfig.screenWidthDp > newConfig.screenHeightDp
        mWm = mActivity?.windowManager
        val dm = DisplayMetrics()
        mWm?.defaultDisplay?.getMetrics(dm)
        mScreenWidth = dm.widthPixels
        mScreenHeight = dm.heightPixels
        if (mIsLandScape) {
            // 横屏
            if (mParams!!.x > 100) {
                val tempX = mScreenHeight - mMarginY / 2
                mParams!!.x = tempX
            }
            mParams?.y = (mScreenWidth - mMarginX).shr(1)
        } else {
            if (mParams!!.x > 100) {
                val tempX = mScreenWidth - mMarginX / 2
                mParams!!.x = tempX
            }
            mParams?.y = (mScreenHeight - mMarginY).shr(1)
        }
        mWm?.updateViewLayout(this, mParams)
    }

    interface OnSuspendClickListener{
        fun onSuspendClick()
    }
}

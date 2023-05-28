/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xui.bottombar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.danny.xui.databinding.BottomBarBinding

/**
 * 底部固定的tab组件
 *
 * @author x
 * @since 2023-05-28
 */
class BottomBar(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {
    private var mListener: BottomListener? = null
    private val mBinding: BottomBarBinding by lazy {
        BottomBarBinding.inflate(LayoutInflater.from(context), this, false)
    }

    init {
        removeAllViews()
        addView(mBinding.root)
        mBinding.xBottomGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                mBinding.bottomHome.id -> mListener?.home()
                mBinding.bottomMessage.id -> mListener?.message()
                mBinding.bottomMe.id -> mListener?.me()
            }
        }
    }

    fun setListener(bottomListener: BottomListener) {
        mListener = bottomListener
    }

    interface BottomListener{
        fun home()
        fun message()
        fun me()
    }
}

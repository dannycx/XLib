package com.danny.xui.title

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.danny.xtool.UiTool
import com.danny.xui.databinding.TitleViewBinding
import com.danny.xui.title.info.XTitleInfo

/**
 * 应用标题组件
 * 2020/05/16
 * create by danny
 */
class TitleView(context: Context, attrs: AttributeSet? = null, def: Int = 0): FrameLayout(context, attrs, def) {
    private val xTitleBinding: TitleViewBinding = TitleViewBinding.inflate(LayoutInflater.from(context))

    fun initView(info: XTitleInfo?) {
        val backRes = info?.backIconRes?: ""
        val titleRes = info?.titleRes?: "app_name"
        val menuRes = info?.menuRes?: ""
        xTitleBinding.xTitleBack.setImageResource(UiTool.backgroundId(context, backRes))
        xTitleBinding.xTitleTv.text = UiTool.getString(context, UiTool.stringId(context, titleRes))
        if (menuRes.isNotBlank())
            xTitleBinding.xTitleMenu.setImageResource(UiTool.backgroundId(context, menuRes))
    }

    fun backClick(clickListener: OnClickListener) = xTitleBinding.xTitleBack.setOnClickListener(clickListener)

    fun menuClick(clickListener: OnClickListener) = xTitleBinding.xTitleMenu.setOnClickListener(clickListener)
}

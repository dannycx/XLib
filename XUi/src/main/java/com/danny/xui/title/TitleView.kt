package com.x.xui.widget.title

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.danny.widget.title.info.XTitleInfo
import com.x.xtools.XUiUtil
import com.x.xui.databinding.XTitleViewBinding

/**
 * 应用标题组件
 * 2020/05/16
 * create by danny
 */
class TitleView(context: Context, attrs: AttributeSet? = null, def: Int = 0): FrameLayout(context, attrs, def) {
    private val xTitleBinding: XTitleViewBinding = XTitleViewBinding.inflate(LayoutInflater.from(context))

    fun initView(info: XTitleInfo?) {
        val backRes = info?.backIconRes?: ""
        val titleRes = info?.titleRes?: "app_name"
        val menuRes = info?.menuRes?: ""
        xTitleBinding.xTitleBack.setImageResource(XUiUtil.getMipmapDrawableColorId(context, backRes))
        xTitleBinding.xTitleTv.text = XUiUtil.getString(context, XUiUtil.getStringId(context, titleRes))
        if (menuRes.isNotBlank())
            xTitleBinding.xTitleMenu.setImageResource(XUiUtil.getMipmapDrawableColorId(context, menuRes))
    }

    fun backClick(clickListener: OnClickListener) = xTitleBinding.xTitleBack.setOnClickListener(clickListener)

    fun menuClick(clickListener: OnClickListener) = xTitleBinding.xTitleMenu.setOnClickListener(clickListener)
}

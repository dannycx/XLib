/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import androidx.core.content.ContextCompat

/**
 * UI工具
 *
 * @author danny
 * @since 2023/5/21
 */
object UiTool {
    const val RES_LAYOUT = "layout"
    const val RES_ID = "id"
    const val RES_STRING = "string"
    const val RES_COLOR = "color"
    const val RES_DRAWABLE = "drawable"
    const val RES_MIPMAP = "mipmap"
    const val RES_DIMEN = "dimen"
    const val RES_STYLE = "style"
    const val RES_ATTR = "attr"
    const val RES_ANIM = "anim"

    /**
     * 获取布局id
     */
    fun layoutId(context: Context, name: String) = resId(context, name, RES_LAYOUT)

    /**
     * 获取viewId
     */
    fun viewId(context: Context, name: String) = resId(context, name, RES_ID)

    /**
     * 获取字符串id
     */
    fun stringId(context: Context, name: String) = resId(context, name, RES_STRING)

    /**
     * 获取颜色id
     */
    fun colorId(context: Context, name: String) = resId(context, name, RES_COLOR)

    /**
     * 获取Drawable id
     */
    fun drawableId(context: Context, name: String) = resId(context, name, RES_DRAWABLE)

    /**
     * 获取Mipmap id
     */
    fun mipmapId(context: Context, name: String) = resId(context, name, RES_MIPMAP)

    /**
     * 获取Dimen id
     */
    fun dimenId(context: Context, name: String) = resId(context, name, RES_DIMEN)

    /**
     * 获取Style id
     */
    fun styleId(context: Context, name: String) = resId(context, name, RES_STYLE)

    /**
     * 获取ATTR id
     */
    fun attrId(context: Context, name: String) = resId(context, name, RES_ATTR)

    /**
     * 获取Anim id
     */
    fun animId(context: Context, name: String) = resId(context, name, RES_ANIM)

    /**
     * 获取资源文件id
     *
     * @param context 上下文
     * @param name 文件名
     * @param identifier 文件类型(layout,string,drawable,mipmap等)
     * @return 资源文件id
     */
    @SuppressLint("DiscouragedApi")
    private fun resId(context: Context, name: String, identifier: String) =
        context.resources.getIdentifier(name, identifier, context.packageName)

    /**
     * 获取mipmap/drawable/color资源id
     */
    fun backgroundId(context: Context, name: String?): Int {
        name?:return android.R.drawable.ic_menu_edit
        var resId = mipmapId(context, name)
        if (resId == 0) {
            resId = drawableId(context, name)
        }
        if (resId == 0) {
            resId = colorId(context, name)
        }
        if (resId == 0) {
            return Color.RED
        }
        return resId
    }

    /**
     * 获取dimen资源
     */
    fun dimensionPixelSize(context: Context, resId: Int) =
        context.resources.getDimensionPixelSize(resId)

    /**
     * px -> dp
     */
    fun px2dp(context: Context, px: Int): Float {
        val density = context.resources.displayMetrics.density
        return px / density + 0.5f
    }

    /**
     * dp -> px
     */
    fun dp2px(context: Context, dp: Float): Int {
        // 方式一
//        val density = context.resources.displayMetrics.density
//        return (dp * density + 0.5f).toInt()

        // 方式二
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics).toInt()
    }

    /**
     * px -> sp
     */
    fun px2sp(context: Context, px: Int): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return px / scaledDensity + 0.5f
    }

    /**
     * sp -> px
     */
    fun sp2px(context: Context, sp: Float): Int {
        // 方式一
//        val scaledDensity = context.resources.displayMetrics.scaledDensity
//        return (sp * scaledDensity + 0.5f).toInt()

        // 方式二
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
            context.resources.displayMetrics).toInt()
    }

    /**
     * 获取屏幕宽高
     *
     * @param context 上下文对象
     * @return 屏幕宽高数组
     */
    fun screenDisplay(context: Context): IntArray {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        wm?: return intArrayOf(0, 0)
        val display = wm.defaultDisplay
        return intArrayOf(display.width, display.height)
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    fun displayWidth(context: Context) =
        context.resources.displayMetrics.widthPixels

    /**
     * 获取屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    fun displayHeight(context: Context) =
        context.resources.displayMetrics.heightPixels

    /**
     * 获取字符串
     */
    fun getString(context: Context, resId: Int) =
        try {
            context.resources.getString(resId)
        } catch (e: NotFoundException) {
            ""
        }

    /**
     * 获取字符串数组
     */
    fun getStringArray(context: Context, resId: Int) =
        try {
            context.resources.getStringArray(resId)
        } catch (e: NotFoundException) {
            arrayOf("")
        }

    /**
     * 获取Drawable
     */
    fun getDrawable(context: Context, resId: Int) =
        try {
            ContextCompat.getDrawable(context, resId)
        } catch (e: NotFoundException) {
            android.R.drawable.ic_menu_edit
        }

    /**
     * 获取颜色
     */
    fun getColor(context: Context, resId: Int) =
        try {
            ContextCompat.getColor(context, resId)
        } catch (e: NotFoundException) {
            Color.RED
        }

    /**
     * 根据id获取颜色的状态选择器
     */
    fun colorStateList(context: Context, resId: Int) =
        try {
            ContextCompat.getColorStateList(context, resId)
        } catch (e: NotFoundException) {
            null
        }

    /**
     * 获取系统认为最小滑动距离TouchSlop,当小于该值时,忽略滑动(8dp)
     */
    fun touchSlop(context: Context) =
//        ViewConfiguration.getWindowTouchSlop()
        ViewConfiguration.get(context).scaledTouchSlop

    /**
     * 加载布局文件
     *
     * @param context 上下文对象
     * @param layoutId 布局id
     * @return view
     */
    fun inflate(context: Context, layoutId: Int) =
        View.inflate(context, layoutId, null)

    /**
     * 对话框弹起时activity改变透明度
     *
     * @param activity activity对象
     * @param visible 透明度是否改变
     */
    fun activityAlpha(activity: Activity, visible: Boolean) {
       val params = activity.window.attributes as WindowManager.LayoutParams
        params.alpha = if (visible) 0.7f else 0.3f
        activity.window.attributes = params
    }

    /**
     * 服务是否运行
     *
     * @param context 上下文对象
     * @param name 服务名字
     * @return 是否开启 true-开启
     */
    fun serviceIsRunning(context: Context, name: String): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        am?:return false
        val runServiceList = am.getRunningServices(20)
        for (service in runServiceList) {
            if (service.service.className == name) {
                return true
            }
        }
        return false
    }
}
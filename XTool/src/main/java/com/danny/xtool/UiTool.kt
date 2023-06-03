/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources.NotFoundException
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process
import android.os.SystemClock
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.nio.charset.Charset
import java.util.Locale
import kotlin.system.exitProcess

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
     * 获取dimen资源
     */
    fun dimensionPixelSize(context: Context, resName: String) =
        context.resources.getDimensionPixelSize(dimenId(context, resName))

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
            ContextCompat.getDrawable(context, android.R.drawable.ic_menu_edit)
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


    /**
     * 曲面屏适配
     *
     * @param activity
     * @param autoConfigId 需要扩充的布局id
     */
    fun setForRing(activity: Activity, autoConfigId: IntArray) {
        var padding = dimensionPixelSize(activity, com.danny.common.R.dimen.dp_8)
        val isLand = (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        if (isLand) {
            padding = 0
        }
        for (id in autoConfigId) {
            val subView = activity.findViewById<View>(id)
            subView?.setPadding(padding, subView.paddingTop, padding, subView.paddingBottom)
        }
    }

    /**
     * 设置默认的margin
     */
    fun setDefaultMargin(activity: Activity, autoConfigId: IntArray?) {
        autoConfigId?.let {
            val margin = activity.resources.getDimensionPixelSize(com.danny.common.R.dimen.dp_24)
            for (id in it) {
                val subView = activity.findViewById<View>(id)
                setMargin(subView, margin, margin)
            }
        }
    }

    fun setNoMargin(activity: Activity, autoConfigId: IntArray?) {
        autoConfigId?.let {
            val margin = activity.resources.getDimensionPixelSize(com.danny.common.R.dimen.dp_0)
            for (id in it) {
                val subView = activity.findViewById<View>(id)
                setMargin(subView, margin, margin)
            }
        }
    }

    private fun setMargin(subView: View?, start: Int, end: Int) {
        subView?.let {
            when (it.layoutParams) {
                is ViewGroup.MarginLayoutParams -> {
                    val mlp = it.layoutParams as ViewGroup.MarginLayoutParams
                    mlp.marginStart = start
                    mlp.marginEnd = end
                    it.layoutParams = mlp
                }
            }
        }
    }

    /**
     * 设置默认padding
     */
    fun setDefaultPadding(activity: Activity, autoConfigId: IntArray?) {
        autoConfigId?.let {
            val padding = activity.resources.getDimensionPixelSize(com.danny.common.R.dimen.dp_8)
            for (id in it) {
                val subView = activity.findViewById<View>(id)
                setPadding(subView, padding, padding)
            }
        }
    }

    private fun setPadding(subView: View?, paddingStart: Int, paddingEnd: Int) {
        subView?.setPadding(paddingStart, subView.paddingTop, paddingEnd, subView.paddingBottom)
    }

    private var startTime: Long = 0
    /**
     * 双击退出应用
     */
    fun exitApp(context: Context) {
        if (System.currentTimeMillis() - startTime < 1000) {
            removeAllPage()
            Process.killProcess(0)
            exitProcess(0)
        } else {
            startTime = System.currentTimeMillis()
            Toast.makeText(context, "再点击一次退出应用", Toast.LENGTH_SHORT).show()
        }
    }

    // 数字为几,就是几击事件
    private val hits = LongArray(3)
    /**
     * 多击事件
     */
    fun multipleClick(context: Context) {
        System.arraycopy(hits, 1, hits, 0, hits.size - 1)
        hits[hits.size - 1] = SystemClock.uptimeMillis()
        if (hits[hits.size - 1] - hits[0] < 1000) {
            // 3击事件处理

        }
    }

    /**
     * 开机启动动画,AnimationDrawable动画图片资源回收
     */
    fun recycleAnimDrawable(drawable: AnimationDrawable?) {
        drawable?:return
        drawable.stop()
        for (i in 0 until drawable.numberOfFrames) {
            val frame = drawable.getFrame(i)
            if (frame is BitmapDrawable) {
                frame.bitmap.recycle()
            }
            frame.callback = null
        }
        drawable.callback = null
    }

    private val mActivity = ArrayList<Activity>()
    fun addActivity(activity: Activity) {
        mActivity.add(activity)
    }

    fun removeActivity(activity: Activity) {
        if (activity in mActivity) {
            mActivity.remove(activity)
        }
    }

    fun removeAllPage() {
        for (activity in mActivity) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        mActivity.clear()
    }

    fun keyBoard(root: View, view: View, isNormal: Boolean) {
        val rect = Rect()
        root.getWindowVisibleDisplayFrame(rect)

        // 不可见高度
        val invisibleHeight = root.rootView.height - rect.bottom
        val keyboardHeight = screenDisplay(root.context)[1] / 4
        if (invisibleHeight > keyboardHeight) {
            if (isNormal) {
                // 上移
                val animator = ObjectAnimator.ofFloat(root, "translationY", 0f, -107f)
                animator.duration = 300
                animator.interpolator = LinearInterpolator()
                animator.start()
            }
        } else {
            // 下移
            if (!isNormal) {
                val animator = ObjectAnimator.ofFloat(root, "translationY", root.translationY, 0f)
                animator.duration = 300
                animator.interpolator = LinearInterpolator()
                animator.start()
            }
        }
    }

    /**
     * 获取设备系统语言
     */
    fun language() = Locale.getDefault().language

    /**
     * 获取系统属性值
     *      -ro.build.version.emui     emui版本>=11(android11.+)
     *      -ro.build.version.magic    荣耀版本>=4(android11.+)
     */
    fun systemProp(key: String): String? {
        var value: String? = null
        var c: Class<*>? = null
        var method: Method? = null
        try {
            c = Class.forName("android.os.SystemProperties")
            method = c.getDeclaredMethod("get", String::class.java)
        } catch (e: ClassNotFoundException) {

        } catch (e: NoSuchMethodException) {

        }
        method?:return null
        value =
            try {
                method.invoke(c, key) as String
            } catch (e: IllegalAccessException) {
                null
            } catch (e: InvocationTargetException) {
                null
            }
        return value
    }

    private var SN: String = ""
    fun sn(): String {
        var isResultValid = TextUtils.isEmpty(SN) || Build.UNKNOWN == SN
        if (isResultValid) {
            try {
                val method = Class.forName("android.os.SystemProperties")
                    .getDeclaredMethod("get", String::class.java)
                SN = method.invoke(null, "ro.serialno") as String
            } catch (e: ClassNotFoundException) {

            } catch (e: NoSuchMethodException) {

            } catch (e: IllegalAccessException) {

            } catch (e: InvocationTargetException) {

            }
            isResultValid =  TextUtils.isEmpty(SN) || Build.UNKNOWN == SN
            if (isResultValid) {
                // 低版本
                SN = Build.USER
            }

            isResultValid =  TextUtils.isEmpty(SN) || Build.UNKNOWN == SN
            if (isResultValid && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    SN = Build.getSerial()
                } catch (e: SecurityException) {

                }
            }
        }
        return SN
    }

    /**
     * 曲面屏适配
     *
     * @param activity 当前activity
     * @param autoConfigId 适配控件id
     */
    fun forRing(activity: Activity, autoConfigId: IntArray) {
        var dp8 = dimensionPixelSize(activity, com.danny.common.R.dimen.dp_8)
        val isLand = activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLand) {
            dp8 = 0
        }
        for (i in autoConfigId.indices) {
            val subView = activity.findViewById<View>(autoConfigId[i])
            subView?.setPadding(dp8, subView.paddingTop, dp8, subView.paddingBottom)
        }
    }

    /**
     * 获取asset资源
     */
    fun assetString(context: Context, name: String): String? {
        val output = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var ch = -1
        var byteData: ByteArray? = null
        var input: InputStream? = null
        try {
            input = context.assets.open(name)
        } catch (e: IOException) {

        }

        input?: return null
        // Read the entire asset into a local byte buffer.
        try {
            ch = input.read(buf)
            while (ch != -1) {
                output.write(buf, 0, ch)
                ch = input.read(buf)
            }
            byteData = output.toByteArray()
            output.close()
        } catch (e: IOException) {

        }
        val data: String? =
            try {
                String(byteData!!, Charset.forName("UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                null
            }
        return data
    }

    /**
     * 集合是否为空
     */
    fun isEmpty(list: Collection<Any?>?): Boolean = list.isNullOrEmpty()

    /**
     * 集合是否为空
     */
    fun isEmpty(map: Map<Any?, Any?>?): Boolean =
        map.isNullOrEmpty() || map.keys.isEmpty()

    /**
     * 当前进程名称
     */
    fun curProcessName(context: Context): String? {
        val pid = Process.myPid()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcessInfo = am.runningAppProcesses.iterator() as Iterator<ActivityManager.RunningAppProcessInfo>
        while (runningProcessInfo.hasNext()) {
            if (runningProcessInfo.next().pid == pid) {
                return runningProcessInfo.next().processName
            }
        }
        return null
    }

    /**
     * 是否是深色主题
     */
    fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
}

inline fun <reified T> startActivity(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}

/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Environment
import android.text.TextUtils
import android.view.View
import androidx.palette.graphics.Palette
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

/**
 * bitmap工具类
 *
 * @author x
 * @since 2023-05-21
 */
object BitmapTool {
    /**
     * 压缩图片
     * 将inJustDecodeBounds设为true
     * 从options读取原图宽高
     * 结合view大小计算采样率
     * 将inJustDecodeBounds设为false从新加载图片
     *
     * @param res Resources对象
     * @param resId 资源id
     * @param width 显示宽
     * @param height 显示高
     */
    fun compressBitmap(res: Resources, resId: Int, width: Int, height: Int): Bitmap {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        val options = Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // 计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height)

        // 使用inSampleSize解析图片
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    /**
     * 计算压缩比
     *
     * @param options bitmap属性
     * @param width 宽
     * @param height 高
     */
    private fun calculateInSampleSize(options: Options, width: Int, height: Int): Int {
        // 原图宽高
        val bitmapWidth = options.outWidth
        val bitmapHeight = options.outHeight
        var inSampleSize = 1
        if (width < bitmapWidth || height < bitmapHeight) {
            // 方式一
            // 计算实际宽高比
            val widthRatio = (bitmapWidth / width).toDouble().roundToInt()
            val heightRatio = (bitmapHeight / height).toDouble().roundToInt()
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio

            // 方式二
//            val halfBitmapWidth: Int = bitmapWidth / 2
//            val halfBitmapHeight: Int = bitmapHeight / 2
//            // 计算缩放比,是2的指数
//            while (halfBitmapWidth / inSampleSize >= width
//                && halfBitmapHeight / inSampleSize >= height) {
//                inSampleSize *= 2
//            }
        }
        return inSampleSize
    }

    /**
     * 纵向拼接bitmap
     *
     * @param first 第一张bitmap
     * @param second 第二张bitmap
     * @return 拼接的bitmap
     */
    fun addBitmap(first: Bitmap, second: Bitmap): Bitmap {
        val width = first.width
        val height = first.height + second.height

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(first, 0f, 0f, null)
        canvas.drawBitmap(second, 0f, first.height.toFloat(), null)
        return bitmap
    }

    /**
     * 生成透明背景的圆形图片
     *
     * @param bitmap 源bitmap
     * @return 透明背景的圆形图片bitmap
     */
    fun createCircleBitmap(bitmap: Bitmap?): Bitmap? {
        bitmap?:return null
        try {
            val circleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(circleBitmap)
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(Rect(0, 0, bitmap.width, bitmap.height))

            // 已短边为准
            val roundPx = if (bitmap.width > bitmap.height) {
                bitmap.height / 2f
            } else {
                bitmap.width / 2f
            }
            paint.isAntiAlias = true
            paint.setARGB(0, 0, 0, 0)
            paint.color = Color.WHITE
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            val src = Rect(0, 0, bitmap.width, bitmap.height)
            canvas.drawBitmap(bitmap, src, rect, paint)
            return circleBitmap
        } catch (e: Exception) {
            return bitmap
        }
    }

    /**
     * view转为bitmap
     */
    fun viewToBitmap(view: View): Bitmap? {
        val width = view.width
        val height = view.height
        when {
            width == 0 || height == 0 -> return null
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        canvas.save()
        return bitmap
    }

    /**
     * 是否为空白图片
     */
    fun isWhiteImg(bitmap: Bitmap): Boolean {
        val b: Bitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        return b.sameAs(bitmap)
    }

    /**
     * 存储文件
     */
    fun saveBitmapToFile(bitmap: Bitmap, filePath: String, fileName: String): CharSequence? {
        var fileOutputStream: FileOutputStream? = null
        try {
            val pathFile = getFilePath(filePath)
            val file = File(pathFile.canonicalPath, fileName)
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            return file.canonicalPath
        } catch (e: Exception) {
            e.toString()
        } finally {
            fileOutputStream?.close()
        }
        return ""

    }

    private fun getFilePath(filePath: String): File {
        var file = if (TextUtils.isEmpty(filePath)) {
            Environment.getExternalStorageDirectory()
        } else {
            File(filePath)
        }
        if (!file.exists()) {
            val mkdirs = file.mkdirs()
            if (!mkdirs) {
                file = Environment.getExternalStorageDirectory()
            }
        }
        return file
    }

    /**
     * bitmap转byteArray
     */
    fun bitmapToArray(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    /**
     * 拾取图片颜色设置到指定控件
     *
     * @param bitmap 图片
     */
    fun palette(bitmap: Bitmap, view: View) {
        Palette.from(bitmap).generate {
            it?:return@generate
            val swatch = it.vibrantSwatch as Palette.Swatch?
            swatch?:return@generate
            view.setBackgroundDrawable(ColorDrawable(swatch.rgb))
        }
    }
}

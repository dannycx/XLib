/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Canvas
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
     *
     * @param res Resources对象
     * @param resId 资源id
     * @param width 显示宽
     * @param height 显示高
     */
    fun decodeSampleBitmapFromResource(res: Resources, resId: Int, width: Int, height: Int): Bitmap {
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
            // 计算实际宽高比
            val widthRatio = (bitmapWidth / width).toDouble().roundToInt()
            val heightRatio = (bitmapHeight / height).toDouble().roundToInt()
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
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

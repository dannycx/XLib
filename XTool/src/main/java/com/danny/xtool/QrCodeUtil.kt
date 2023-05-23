/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import com.danny.xtool.executor.ThreadExecutor
import com.danny.xtool.executor.UiExecutor
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 二维码生成工具类
 *
 * @author x
 * @since 2023-05-23
 */
object QrCodeUtil {
    /**
     * 创建二维码
     */
    fun createQrCode(url: String, width: Int, height: Int): Bitmap? {
        try {
            var matrix = QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height) as BitMatrix
            matrix = deleteWhite(matrix)
            val qrWidth = matrix.width
            val qrHeight = matrix.height
            val pixels = IntArray(qrWidth * qrHeight)
            for (y in 0 until qrHeight) {
                for (x in 0 until qrWidth) {
                    if (matrix.get(x, y)) {
                        pixels[y * qrWidth + x] = Color.BLACK
                    } else {
                        pixels[y * qrWidth + x] = Color.WHITE
                    }
                }
            }

            val bitmap = Bitmap.createBitmap(qrWidth, qrHeight, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, qrWidth, 0, 0, qrWidth, qrHeight)
            return bitmap
        } catch (e: Exception) {
            Log.e("", "e: $e")
            return null
        }
    }

    private fun deleteWhite(matrix: BitMatrix): BitMatrix {
        val rec = matrix.enclosingRectangle
        val resWidth = rec[2] + 1
        val resHeight = rec[3] + 1

        val resMatrix = BitMatrix(resWidth, resHeight)
        resMatrix.clear()
        for (i in 0 until resWidth) {
            for (j in 0 until resHeight) {
                if (matrix.get(i + rec[0], j + rec[1])) {
                    resMatrix.set(i, j)
                }
            }
        }
        return resMatrix
    }

    fun createQRImage(content: String?, width: Int, height: Int, logoBm: Bitmap?, filePath: String): Boolean {
        content?:return false
        if (content.isEmpty()) return false
        try {
            // 配置参数
            val hints: MutableMap<EncodeHintType, Any> = HashMap()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"

            // 容错级别
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

            // 设置空白边距的宽度，默认值4
            hints[EncodeHintType.MARGIN] = 1

            // 图像数据转换，使用了矩阵转换
            val bitMatrix: BitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val pixels = IntArray(width * height)

            // 下面这里按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = -0x1000000
                    } else {
                        pixels[y * width + x] = -0x1
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            var bitmap: Bitmap? = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap?.setPixels(pixels, 0, width, 0, 0, width, height)
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm)
            }

            // 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(filePath))
        } catch (e: WriterException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun addLogo(src: Bitmap?, logo: Bitmap?): Bitmap? {
        if (src == null) {
            return null
        }
        if (logo == null) {
            return src
        }

        // 获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoWidth = logo.width
        val logoHeight = logo.height
        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src
        }

        // logo大小为二维码整体大小的1/5
        val scaleFactor = srcWidth * 1.0f / 5 / logoWidth
        var bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        try {
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2.0f, srcHeight / 2.0f)
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2.0f, (srcHeight - logoHeight) / 2f, null)
//            canvas.save(Canvas.ALL_SAVE_FLAG)
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            bitmap = null
            e.stackTrace
        }
        return bitmap
    }

    /**
     * 显示二维码
     *
     * @param activity
     * @param url
     * @param iv
     * @param logoId 二维码中心的logo资源id
     */
    fun showQrImage(activity: Activity, url: String, iv: ImageView, logoId: Int) {
        ThreadExecutor.getInstance().execute {
            val qrPath = activity.filesDir.absolutePath + File.separator + System.currentTimeMillis() + ".PNG"
            val result = createQRImage(url, 300, 300, BitmapFactory.decodeResource(activity.resources,
                logoId), qrPath)
            if (result) {
                UiExecutor.getInstance().execute {
                    iv.setImageBitmap(BitmapFactory.decodeFile(qrPath))
                }
            }
        }
    }

    /**
     * 显示二维码
     *
     * @param activity
     * @param url
     * @param iv
     */
    fun showQrImage(activity: Activity, url: String, iv: ImageView) {
        ThreadExecutor.getInstance().execute {
            val qrPath = activity.filesDir.absolutePath + File.separator + System.currentTimeMillis() + ".PNG"
            Log.i("", "qrPath: $qrPath")
            val result = createQRImage(url, 300, 300, null, qrPath)
            if (result) {
                UiExecutor.getInstance().execute {
                    iv.setImageBitmap(BitmapFactory.decodeFile(qrPath))
                }
            }
        }
    }

    /**
     * 显示二维码
     *
     * @param url
     * @param iv
     */
    fun showQrImage(url: String, iv: ImageView) {
        ThreadExecutor.getInstance().execute {
            val bitmap = createQrCode(url, 300, 300)
            UiExecutor.getInstance().execute {
                iv.setImageBitmap(bitmap)
            }
        }
    }
}

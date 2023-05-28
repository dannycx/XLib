/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.shotscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import com.danny.xtool.BitmapTool
import java.lang.ref.WeakReference

class ScreenFragment: Fragment() {
    private val REQUEST_CODE = 2
    private val RESULT_OK = -1

    private lateinit var mpm: MediaProjectionManager
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var densityDpi: Int = 0
    private var mp: MediaProjection? = null
    private var imageReader: ImageReader? = null
    private var resultData: Intent? = null
    private var resultCode: Int = 0
    private var vd: VirtualDisplay? = null
    private var screenFinish: XOnScreenFinish? = null
    private var asyncTask: ImageToBitmapAsyncTask? = null

    interface XOnScreenFinish {
        fun success(bitmap: Bitmap)
        fun failure()
        fun whiteFailure()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mpm = activity?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        setImageReader()
    }

    @SuppressLint("WrongConstant")
    private fun setImageReader() {
        val dm = Resources.getSystem().displayMetrics
        densityDpi = dm.densityDpi
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
        imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 1)
    }

    private fun setMediaProjection() {
        resultData?.let {
            mp = mpm.getMediaProjection(resultCode, it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE == requestCode) {
            if (resultCode != RESULT_OK) {
                return
            }
            this.resultCode = resultCode
            if (data != null) {
                resultData = data
                setMediaProjection()
            }

            Handler().postDelayed(this::setVirtualDisplay, 600)
        }
    }

    private fun setVirtualDisplay() {
        if (null != asyncTask && asyncTask?.isCancelled == true) {
            return
        }
        vd = mp?.createVirtualDisplay("screenCaptor", screenWidth, screenHeight
            , densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR
            , imageReader?.surface, null, null)

        var image: Image? = null
        do {
            image = imageReader?.acquireNextImage()
        } while (null == image)
        asyncTask = ImageToBitmapAsyncTask()
        screenFinish?.let {
            asyncTask?.setListener(it)
        }
        asyncTask?.execute(image)

    }

    fun startCapture(listener: XOnScreenFinish) {
        screenFinish = listener
        when {
            null == mp -> {
                setVirtualDisplay()
            }
            resultCode != 0 && null != resultData -> {
                setMediaProjection()
                setVirtualDisplay()
            }
            else -> {
                requestScreenPermission()
            }
        }
    }

    private fun requestScreenPermission() {
        activity?.startActivityForResult(mpm.createScreenCaptureIntent(), REQUEST_CODE)
    }

    @SuppressLint("StaticFieldLeak")
    inner class ImageToBitmapAsyncTask(): AsyncTask<Image, Void, Bitmap>() {
        private var isRunning = false
        private lateinit var weakReference: WeakReference<XOnScreenFinish>

        fun setListener(listener: XOnScreenFinish) {
            weakReference = WeakReference<XOnScreenFinish>(listener)
        }
        override fun onPreExecute() {
            super.onPreExecute()
            isRunning = true
        }

        override fun doInBackground(vararg params: Image?): Bitmap? {
            if (params.isEmpty()) {
                return null
            }
            val img = params[0]

            try {
                val width = img?.width ?: 0
                val height = img?.height ?: 0
                val plans = img?.planes
                val buffer = plans?.get(0)?.buffer
                val pixelStride = plans?.get(0)?.pixelStride ?: 0
                val rowStride = plans?.get(0)?.rowStride ?: 0
                val rowPadding = rowStride - pixelStride * width
                val bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
                bitmap.copyPixelsFromBuffer(buffer)
                return Bitmap.createBitmap(bitmap, 0, 0, width, height)
            } catch (e: Exception) {
                return null
            } finally {
                img?.close()
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            isRunning = true
            val listener = weakReference.get() ?: return

            if (null == result) {
                listener.failure()
            } else {
                if (BitmapTool.isWhiteImg(result)) {
                    listener.whiteFailure()
                    return
                }
                listener.success(result)
            }
        }

        override fun onCancelled() {
            isRunning = false
        }

        fun isRun() : Boolean {
            return isRunning
        }

    }

    override fun onPause() {
        super.onPause()
        stopMP()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (asyncTask?.isRun() == false) {
            asyncTask?.cancel(true)
            asyncTask = null
        }
        releaseVirtualDisplay()
    }

    private fun stopMP() {
        mp?.stop()
        mp = null
    }

    private fun releaseVirtualDisplay() {
        vd?.release()
        mp = null
    }
}

/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.shotscreen

import android.content.Context
import android.graphics.Bitmap
import android.view.View

class ScreenListener: ViewCallback, ScreenFragment.XOnScreenFinish {
//    private var screenFragment: XScreenFragment? = null
//    private lateinit var activity: AppCompatActivity
//    private val TAG = "CAPTURE_TAG"

//    fun isAdd() {
//
//    }
//
    override fun success(bitmap: Bitmap) {
//        val filePath = XBitmapUtil.saveBitmapToFile(bitmap
//            , Environment.getExternalStorageDirectory().absolutePath + "/screenshot"
//            , System.currentTimeMillis().toString() + ".png")
//        if (TextUtils.isEmpty(filePath)) {
//
//        } else {
//
//        }
    }

    override fun failure() {
//        TODO("Not yet implemented")
    }
//
    override fun whiteFailure() {
//        if (screenFragment != null && screenFragment?.isAdded == true) {
//            screenFragment?.startCapture(this)
//        }
    }
//
    override fun onAdd(context: Context) {
//        if (context is AppCompatActivity) {
//            activity = context
//            val fm = activity.supportFragmentManager
//            val fragment = fm.findFragmentByTag(TAG)
//            if (fragment is XScreenFragment) {
//                screenFragment = fragment
//            } else{
//                screenFragment = XScreenFragment()
//                screenFragment?.let {
//                    fm.beginTransaction().add(it, TAG).commit()
//                }
//            }
//        }

//        TODO("Not yet implemented")
    }

    override fun onClick(view: View) {
//        if (screenFragment != null && screenFragment?.isAdded == true) {
//            screenFragment?.startCapture(this)
//        }
    }
}

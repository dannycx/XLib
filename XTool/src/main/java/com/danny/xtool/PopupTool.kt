/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.view.WindowManager.BadTokenException

/**
 * 弹框工具：对话框，进度条
 *
 * @author danny
 * @since 2023-05-28
 */
object PopupTool {


    private val LOCK = Any()
    private var progressDialog: ProgressDialog? = null

    /**
     * 显示进度条
     */
    fun showProgress(context: Context?, msg: String?) {
        if (context !is Activity || context.isFinishing || context.isDestroyed) {
            return
        }
        synchronized(LOCK) {
            try {
                progressDialog = null
                progressDialog = ProgressDialog.show(context, null, msg)
                progressDialog?.setCanceledOnTouchOutside(false)
                progressDialog?.setCancelable(true)
            } catch (e: BadTokenException) {

            }
        }
    }

    /**
     * 隐藏进度条
     */
    fun dismissProgress() {
        synchronized(LOCK) {
            if (progressDialog?.isShowing != true) {
                return
            }
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }
}

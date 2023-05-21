/*
 * Copyright (c) 2023 x
 */

package com.danny.xtool

import android.app.Activity
import android.content.DialogInterface.OnClickListener
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import java.lang.ref.WeakReference

/**
 * 权限申请
 *
 * @author danny
 * @since 2023/5/20
 */
object PermissionTool {
    private lateinit var mReference: WeakReference<Activity>

    fun init(activity: Activity) {
        mReference = WeakReference(activity)
    }

    /**
     * 权限申请
     *
     * @param permission 权限列表
     * @param requestCode 请求码
     */
    fun request(permission: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        val permissionList = checkPermission(permission)
        if (permissionList.size == 0) {
            return
        }
        val permissionArray: Array<String> = permissionList.toTypedArray()
        if (shouldShowPermission(permissionArray)) {
            showMsg("You need allow to " + permissionArray[0]) { _, _ ->
                mReference.get()?.let {
                    ActivityCompat.requestPermissions(it, permissionArray, requestCode)
                }
            }
            mReference.get()?.let {
                ActivityCompat.requestPermissions(it, permissionArray, requestCode)
            }
        }
    }

    /**
     * 弹出对话框，针对用户点击拒绝不再提醒选择，增强用户体验效果
     */
    private fun showMsg(msg: String, listener: OnClickListener) {
        mReference.get()?.let {
            AlertDialog.Builder(it)
                .setMessage(msg)
                .setPositiveButton("ok", listener)
                .create()
                .show()
        }
    }

    /**
     * 校验是否已授权，未授权添加至数组申请授权
     */
    private fun checkPermission(permission: Array<String>): ArrayList<String> {
        val list = ArrayList<String>()
        if (!::mReference.isInitialized) {
            return list
        }
        for (p in permission) {
            mReference.get()?.let {
                if (ActivityCompat.checkSelfPermission(it, p)
                    != PackageManager.PERMISSION_GRANTED) {
                    list.add(p)
                }
            }
        }
        return list
    }

    /**
     * 对权限做选择不再提醒处理
     */
    private fun shouldShowPermission(permission: Array<String>): Boolean {
        if (!::mReference.isInitialized) {
            return false
        }
        var flag = false
        for (p in permission) {
            mReference.get()?.let {
                flag = flag || ActivityCompat.shouldShowRequestPermissionRationale(it, p)
            }
        }
        return flag
    }
}

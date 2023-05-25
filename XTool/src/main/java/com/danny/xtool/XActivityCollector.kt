package com.x.xtools

import android.app.Activity
import android.content.Context
import android.os.Process
import android.widget.Toast
import java.util.*
import kotlin.system.exitProcess

/**
 * activity工具
 */
object XActivityCollector {
    private val activityList = ArrayList<Activity>()
    private var startTime: Long = 0

    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity) {
        if (activity in activityList) {
            activityList.remove(activity)
        }
    }

    fun removeAll() {
        for(activity in activityList) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activityList.clear()
    }

    /** 双击退出应用  */
    fun doubleExit(context: Context?) {
        if (System.currentTimeMillis() - startTime < 1000) { // 双击
            removeAll()
            Process.killProcess(0)
            exitProcess(0)
        } else {
            startTime = System.currentTimeMillis()
            Toast.makeText(context, "再点击一次退出应用", Toast.LENGTH_SHORT).show()
        }
    }
}
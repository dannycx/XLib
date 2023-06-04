package com.danny.xbase.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.danny.xtool.StatusBarTool

/**
 * 基类
 */
open class BaseActivity: AppCompatActivity() {
    private lateinit var receiver: XForceOfflineReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        XActivityCollector.addActivity(this)
    }

    override fun onResume() {
        super.onResume()
        if (needStatusBar()) {
            StatusBarTool.setStatusBarColor(this, resources.getColor(getStatusBarId()), true)
        }

        offLinReceiver()
    }

    private fun getStatusBarId(): Int = com.danny.common.R.color.color_ffffff

    private fun needStatusBar(): Boolean = true

    private fun offLinReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("")
        receiver = XForceOfflineReceiver()
        registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        super.onDestroy()
//        XActivityCollector.removeActivity(this)
    }


    inner class XForceOfflineReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
//            XActivityCollector.finishAll()// 销毁所有activity
            // 跳转登录页
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isHideSoftKey(v, ev)) {
                hideSoftKey(v?.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isHideSoftKey(v: View?, ev: MotionEvent): Boolean {
        var flag = false
        v?.let {
            val location = intArrayOf(0, 0)
            it.getLocationInWindow(location)
            if (it is android.widget.EditText) {
                val l = location[0]
                val t = location[1]
                val r = l + it.width
                val b = t + it.height
                flag = ev.x <= l || ev.x >= r || ev.y <= t || ev.y >= b
            }
        }
        return flag
    }

    private fun hideSoftKey(token: IBinder?) {
        token?.let {
            val m = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            m.hideSoftInputFromWindow(it, 2)
        }
    }

}
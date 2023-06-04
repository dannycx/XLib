package com.danny.demo.home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.danny.xbase.base.scan.ScanBroadcast
import com.danny.xbase.module.X
import com.danny.xtool.crash.CrashHandler
import com.danny.xtool.shotscreen.ShortScreenCallback
import com.danny.xtool.shotscreen.ShotScreenCapture
import com.danny.xui.ScreenShotWindow

class XApp: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        Thread.setDefaultUncaughtExceptionHandler(CrashHandler())

        ScanBroadcast.initScanBroadcastReceiver(this)

        X.init(this)

        initBackground()
    }

    private fun initBackground() {

        val hashMap = HashMap<Activity, ShotScreenCapture>()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                val shotCapture = ShotScreenCapture(activity.contentResolver)
                shotCapture.startShotScreen(object : ShortScreenCallback {
                    override fun successCapture(filePath: String) {
                        val intent = Intent(context, ScreenShotWindow::class.java)
                        intent.putExtra("image_path", filePath)
                        startActivity(intent)
                    }
                })
                hashMap[activity] = shotCapture

            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
                val shotCapture = hashMap[activity]
                shotCapture?.unregisterShotScreen()
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }
}
/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.component

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

/**
 * 通知
 */
object NotifyTool {

    /**
     * 前台服务通知
     */
    fun notification(activity: Service, channelId: String, channelName: String,
        clazz: Class<Activity>, contentTitle: String, contentText: String,
        @DrawableRes smallIcon: Int, @DrawableRes largeIcon: Int) {
        val manager = activity
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName
                , NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(activity, clazz)
        val pi = PendingIntent.getActivity(activity, 0, intent, 0)

        val notification = NotificationCompat.Builder(activity, channelId)
            .setContentTitle(contentTitle).setContentText(contentText)
            .setSmallIcon(smallIcon)
            .setLargeIcon(BitmapFactory.decodeResource(activity.resources, largeIcon))
            .setContentIntent(pi).build()

        activity.startForeground(1, notification)
    }
}

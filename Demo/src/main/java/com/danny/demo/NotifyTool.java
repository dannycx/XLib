package com.danny.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.app.NotificationCompat.Builder;
import android.widget.RemoteViews;

/**
 * Created by 75955 on 2018/10/21.
 * 优先级：
 *  Notification.VISIBILITY_PUBLIC 任何情况
 *  Notification.VISIBILITY_PRIVATE 未锁屏
 *  Notification.VISIBILITY_SECRET 在pin、password、未锁屏
 *  builder.setVisibility(Notification.VISIBILITY_PRIVATE);
 */

public class NotifyTool {
    private static final int NOTIFICATION_ID = 1010;

    /**
     * 发一个简单通知
     *
     * @param activity
     * @param title
     * @param content
     */
    public static void simplyNotify(Context activity, String title, String content) {
        Builder builder = new Builder(activity);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));// 跳转页面
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent,
            PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(com.danny.xui.R.mipmap.icon_notify);
        builder.setContentTitle(title);// 标题
        builder.setContentText(content);
        builder.setAutoCancel(true);// 自动取消
        NotificationManager manager =
            (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * 折叠式通知
     *
     * @param activity
     */
    public static void notify(Context activity) {
        Builder builder = new Builder(activity);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));// 跳转页面
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent,
            PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(com.danny.xui.R.mipmap.icon_notify);
        builder.setAutoCancel(true);// 自动取消
        builder.setContentTitle("折叠式通知");// 标题
        Notification notification = builder.build();
        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews =
            new RemoteViews(activity.getPackageName(), R.layout.notify_open_layout);// 自定义通知视图
//        notification.contentView = remoteViews;// 普通通知自定义视图
        notification.bigContentView = remoteViews;// 指定展开时视图
        NotificationManager manager =
            (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }



    /**
     * 悬停式通知
     *
     * @param activity
     */
    public static void notify(Activity activity, String title) {
        Builder builder = new Builder(activity);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));// 跳转页面
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent,
            PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(com.danny.xui.R.mipmap.icon_notify);
        builder.setAutoCancel(true);// 自动取消
        builder.setContentTitle(title);// 标题

        // 设置点击跳转
        Intent hangIntent = new Intent();
        hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        hangIntent.setClass(activity, activity.getClass());
        PendingIntent pi = PendingIntent.getActivity(activity, 0, hangIntent,
            PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setFullScreenIntent(pi, true);// 将通知设置为悬浮式通知
        NotificationManager manager =
            (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}

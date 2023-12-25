# 四大组件问题记录

**启动服务报错AndroidRuntime:android.app.RemoteServiceException:Context.startForegroundService() did not then call Service.startForeground():**
* android8.0不允许创建后台服务时使用startService()，会引发IllegalStateException。
* 解决：将startService() -> startForegroundService()启动，把后台服务变为前台服务。
```
// 1. 启动
fun adapterStartService(context: Context) {
    val service = ComponentName("com.dcxing.xxx", "com.dcxing.xxx.XxxService")
    val intent = Intent()
    intent.setComponent(service)
    // val intent = Intent(context, XxxService::java.class)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}

// 2. XxxService.class
class XxxService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(100, createNotify())
        }
    }

    private fun createNotify(): Notification {
        val build: Notification.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
                val channel = NotificationChannel("channelId", "name", NotificationManager.IMPORTANCE_LOW)
                manager?.createNotificationChannel(channel)
                Notification.Builder(context, channelId)
            } else {
                Notification.Builder(context)
            }
        return build.setContentTitle("")
            .setContentText("")
            .setAutoCancel(true)
            .build()
    }
}

// 3. AndroidManifest.xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.dcxing.xxx">

    <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>

    ...
</manifest>
```


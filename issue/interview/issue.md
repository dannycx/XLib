# 问题记录

## java.lang.UnsatisfiedLinkError: dlopen failed: "/data/app/~/com.xxx/lib/arm64/libc++_shared.so" has bad ELF magic: 00000000
* 接入MyScript OCR识别so库报错

1.build.gradle移除packagingOptions配置
```
android {
    packagingOptions {
        pickFirst 'lib/x86/libc++_shared.so'
        pickFirst 'lib/arm64-v8a/libc++_shared.so'
        pickFirst 'lib/x86_64/libc++_shared.so'
        pickFirst 'lib/armeabi-v7a/libc++_shared.so'
    }
    ...
}
```

2.添加MyScript混淆(proguard-rules.pro)
```
-keep class com.myscript.** {*;}
```

## Context.startForegroundService() did not then call Service.startForeground()
* Android 8.0+系统不允许后台应用创建后台服务。引入startForeground()
1. 服务
```
class XService: Service() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(100, createNotify(this, "x_id", "name"));
        }
        ...
    }

    ...

    fun createNotify(context: Context, channelId: String, name: String): Notification {
        val build: Notification.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
                val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_LOW)
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
```
2. 启动服务
```
fun startService(context: Context) {
    val intent = Intent(context, XService::class.java)
    intent.action = Intent.ACTION_SCREEN_OFF
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}
```

## Android Studio升级问题
1.java版本问题
```
A problem occurred evaluating project ':android'.
> Failed to apply plugin 'com.android.internal.application'.
   > Android Gradle plugin requires Java 11 to run. You are currently using Java 1.8.
     You can try some of the following options:
       - changing the IDE settings.
       - changing the JAVA_HOME environment variable.
       - changing `org.gradle.java.home` in `gradle.properties`.
```
* 修改：File->Project Structure->SDK location->JDK location...(更改对应jdk版本)
* gradle.properties：org.gradle.java.home=C\:\\Program Files\\Java\\jdk-11.0.6
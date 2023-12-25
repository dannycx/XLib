# 问题记录

**java.Tang.SecurityException: Permission Denial:this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.NTERACT_ACROSS_USERS**
```
try {
    Class<?> activityManager = Class.forName("android.app.ActivityManager");
    int currentUserId = (int)activityManager.getMethod("getCurrentUser").invoke(activityManager);
    Method methodOf = UserHandle.class.getMethod("of",int.class);
    UserHandle userHandle = (UserHandle)methodOf.invoke(null,currentUserId);
    context.sendBroadcastAsUser(new Intent(context.getString(R.string.broadcast_action_app_foreground)),userHandle);
} catch (Exception e) {
    Log.e("", "e: " + e);
}

// 使用sendBroadcastAsUser发广播需新增权限
<uses-permission android:name="android.permission.INTERACT_ACROSS_USERS_FULL"
    tools:ignore="ProtectedPermissions"/>
<uses-permission android:name="android.permission.INTERACT_ACROSS_USERS"
    tools:ignore="ProtectedPermissions"/>
```

## Could not write file to D:\workplace\flutter_project\x_write\build\app\intermediates\flutter\debug\flutter_assets\shaders/ink_sparkle.frag.spirv
* 退出向日葵软件

## git clong https://github.com/xxx/xxx.git 时报错
* Unnamed repository; edit this file 'description' to name the repository.
1. 下载目录存在.git文件删除后clone代码
2. 新建目录clone代码

## 多任务窗口不显示该应用
* luanch activity增加属性：android:excludeFromRecents="true"

## 'compileDebugJavaWithJavac' task (current target is 1.8) and 'kaptGenerateStubsDebugKotlin' task (current target is 17) jvm target compatibility should be set to the same Java version.
* 表示执行compileDebugJavaWithJavac任务时，Java编译版本为1.8，而执行kaptGenerateStubsDebugKotlin任务时，kotlin编译版本为17，它们应设为相同版本
```
解决：build.gradle
plugins {
    ...
    id 'kotlin-kapt'
}

android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = '17'
    }
}
```

## Scheduling restart of crashed servic
* 应用进程异常被杀，服务保活触发重启，服务启动访问未初始化资源或已被杀对象导致循环重启(ANR)
```
服务启动模式：onStartCommand
START_STICKY:被杀后系统自动重启拉活，不会保留之前参数
START_STICKY_COMPATIBILITY:START_STICKY的兼容模式，不保证服务被杀后系统自动拉活
START_NOT_STICKY:服务被杀后不会自动拉活
START_REDELIVER_INTENT:服务被杀后会被系统拉活，并保留之前intent参数

修改：
fun onStartCommand(intent: Intent, flags: Int, startId: Int) {
    ...
    return START_NOT_STICKY
}
```

## For security reasons, WebView is not allowed in privileged processes
* 当应用app申请系统应用，AndroidManifest.xml添加如下配置
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.xxx.xxx"
    android:sharedUserId="android.uid.system">
    ...
</manifest>
```

* 解决方法：加载webView前做如下处理
```
Android 8.0安全机制，通过Hook异常前赋值sProviderInstance解决

public static void hookWebView() {
    int sdkInt = Build.VERSION.SDK_INT;
    try {
        Class<?> factoryClass = Class.forName("android.webkit.WebViewFactory");
        Field field = factoryClass.getDeclaredField("sProviderInstance");
        field.setAccessible(true);
        Object sProviderInstance = field.get(null);
        if (sProviderInstance != null) {
            Log.i("WebView", "sProviderInstance isn't null");
           return;
        }

        Method getProviderClassMethod;
        if (sdkInt > 22) {
            getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass");
        } else if (sdkInt == 22) {
            getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
        } else {
            Log.i("WebView", "Don't need to Hook WebView");
            return;
        }
        getProviderClassMethod.setAccessible(true);
        Class<?> factoryProviderClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
        Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
        Constructor<?> delegateConstructor = delegateClass.getDeclaredConstructor();
        delegateConstructor.setAccessible(true);
        if (sdkInt < 26) {
            Constructor<?> providerConstructor = factoryProviderClass.getConstructor(delegateClass);
            if (providerConstructor != null) {
                providerConstructor.setAccessible(true);
                sProviderInstance = providerConstructor.newInstance(delegateConstructor.newInstance());
            }
        } else {
            Field chromiumMethodName = factoryClass.getDeclaredField("CHROMIUM_WEBVIEW_FACTORY_METHOD");
            chromiumMethodName.setAccessible(true);
            String chromiumMethodNameStr = (String) chromiumMethodName.get(null);
            if (chromiumMethodNameStr == null) {
                chromiumMethodNameStr = "create";
            }
            Method staticFactory = factoryProviderClass.getMethod(chromiumMethodNameStr, delegateClass);
            if (staticFactory != null) {
                sProviderInstance = staticFactory.invoke(null, delegateConstructor.newInstance());
            }
        }

        if (sProviderInstance != null) {
            field.set("sProviderInstance", sProviderInstance);
            Log.i("WebView", "Hook success!");
        } else {
            Log.i("WebView", "Hook failed!");
        }
    } catch (Throwable e) {
        Log.e("WebView", "throwable = " + e.getMessage());
    }
}
```

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

![解决](https://github.com/dannycx/XLib/blob/main/issue/interview/issues_a_b_c_s.md)


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

# 四大组件之Activity

## Activity启动
### 隐藏app launcher图标并隐式启动
+ 入口activity声明为接收隐式intent方式启动，不显示启动图标
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:name=".App"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Demo">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.LAUNCHER" />

                <!-- 关键代码，入口activity声明为接收隐式intent方式启动，不显示启动图标 -->
                <data android:host="DemoActivity" android:scheme="com.dcxing.demo"/>
            </intent-filter>
        </activity>
    </application>

</manifest>

// 启动
val data = Uri.parse("com.dcxing.demo://DemoActivity")
val intent = intent()
intent.data = data
try {
	startActivity(intent)
} catch (e: Exception) {
	LogTool.e("Exception: ${e.message}")
}
```

### 最近任务列表窗口不显示app
+ activity增加属性多任务列表隐藏该应用
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:name=".App"
        android:supportsRtl="true"
        android:theme="@style/Theme.Demo">
        <activity
            android:name=".MainActivity"
            <!-- 关键代码，增加该属性多任务列表隐藏该应用 -->
            android:excludeFromRecents="true"
            android:exported="true">
            <intent-filter>
                ...
            </intent-filter>
        </activity>
    </application>

</manifest>
```

## 退出app
1. 关闭所有打开的activity
2. 调用exitProcess(0) 或 Process.killProcess(Process.myPid())
### 方式一
```
import android.app.Activity

object ActivityTool {
    @JvmStatic
    private val activityList = arrayListOf<Activity>()
    
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }
    
    fun removeActivity(activity: Activity) {
        activityList.remove(activity)
    }
    
    fun finishAll() {
        if (activityList.isEmpty()) {
            return
        }
        for (activity in activityList) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activityList.clear()
    }
}
```

### 方式二
+ finishAffinity关闭所有activity
```
// Activity.class
finishAffinity()
// Fragment.class
activity.finishAffinity()
```

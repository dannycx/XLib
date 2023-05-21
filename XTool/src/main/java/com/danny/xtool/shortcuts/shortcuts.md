# Shortcuts快捷方式

## 方式一：创建静态快捷方式
1. 在 AndroidManifest.xml 中找到主Activity，添加如下配置
```
<activity android:name=".MainActivity">
     <intent-filter>
       <action android:name="android.intent.action.MAIN" />
       <category android:name="android.intent.category.LAUNCHER" />
     </intent-filter>

     <meta-data android:name="android.app.shortcuts"
         android:resource="@xml/shortcuts" />
</activity>
```
2. 创建新的资源文件：res/xml/shortcuts.xml
```
<?xml version="1.0" encoding="utf-8"?>
   <shortcuts xmlns:android="http://schemas.android.com/apk/res/android">
       <shortcut
           android:enabled="true"
           android:icon="@drawable/icon"
           android:shortcutDisabledMessage="@string/msg"
           android:shortcutId="staticId"
           android:shortcutLongLabel="@string/shortcut_long_label"
           android:shortcutShortLabel="@string/shortcut_short_label">
           <intent
               android:action="android.intent.action.VIEW"
               android:data="content://danny.com/fromStaticShortcut"
               android:targetClass="com.danny.xlib.ShortcutsActivity"
               android:targetPackage="com.danny.xlib" />
           <categories android:name="android.shortcut.conversation" />
       </shortcut>
   </shortcuts>
```


## 方式二：创建动态快捷方式



## 方式三
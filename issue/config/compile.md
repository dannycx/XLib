# Android Studio编译问题

## 新建lib无R文件生成
* AndroidManifest.xml
```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.xxx.xxx"/>
```

## Execution failed for task ‘:app:kaptDebugKotlin
+ build.gradle
```
kapt -> annotationProcessor
```

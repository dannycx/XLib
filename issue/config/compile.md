# Android Studio编译问题

## Can't determine type for tag '<macro name="m3_comp_assist_chip_container_shape">?attr/shapeAppearanceCornerSmall</macro>'
* 依赖版本问题：androidx.appcompat:appcompat:1.4.1

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

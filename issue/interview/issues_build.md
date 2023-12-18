# Android Studio构建问题

**Android Studio执行Run安装成功，但启动的还是原应用页面**
* AS -> File -> Invalidate Caches ... -> Invalidate and Restart -> Run

**java.lang.NoClassDefFoundError：Lorg/apache/http/ProtocolVersion 解析失败**
```
# AndroidManifest.xml添加配置
// Android 6.0 中，已移除对 Apache HTTP 客户端的支持。Android 9 开始，默认情况下对应用不可用（bootclass路径）。
<uses-library android:name="org.apache.http.legacy" android:required="false" />
```

**More than one file was found with OS independent path 'lib/x86/libc++_shared.so'**
```
android引入三方库时，本地项目与三方库同时引用相同的.so文件 或 三方库引入了相同的.so文件，导致编译时不知以哪个为主。
修改如下：android/app/build.gradle的android{}块添加代码
...
android {
    packagingOptions {
        pickFirst 'lib/x86/libc++_shared.so'
        pickFirst 'lib/arm64-v8a/libc++_shared.so'
        pickFirst 'lib/x86_64/libc++_shared.so'
        pickFirst 'lib/armeabi-v7a/libc++_shared.so'
        // dex { // dex文件压缩
            // useLegacyPackaging true
        // }
    }
    ...
}
...
```



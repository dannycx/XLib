# 问题记录

## java.lang.UnsatisfiedLinkError: dlopen failed: "/data/app/~~***==/com.xxx-***==/lib/arm64/libc++_shared.so" has bad ELF magic: 00000000
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

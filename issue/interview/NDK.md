# NDK

**java.lang.UnsatisfiedLinkError: dlopen failed: "/data/xxxxx.so" has bad ELF magic**
* so库兼容问题，强制ndk兼容armeabi-v7a架构
```
// build.gradle
...

android {
    defaultConfig {
        ndk {
	        abiFilter "armeabi-v7a"
	        // armeabi-v7a：市场占有率高
	        // 'arm64-v8a'：向下兼容，可兼容armeabi-v7a
	        // armeabi：性能低，不支持浮点运算，Android2.x已废弃
	        // x86/x86_64：市场占有率低
	    }
    }
}

...
```


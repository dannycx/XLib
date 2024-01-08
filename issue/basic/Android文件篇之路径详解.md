# Android文件篇之路径详解
## Environment获取
```
fun filePath() {
    // 获取根目录/data内部存储路径
    LogTool.i("Environment#getDataDirectory: ${Environment.getDataDirectory()}")

    // 获取缓存目录/data/cache
    LogTool.i("Environment#getDownloadCacheDirectory: ${Environment.getDownloadCacheDirectory()}")

    // 获取sd卡目录/storage/emulated/0     /mnt/sdcard
    LogTool.i("Environment#getExternalStorageDirectory: ${Environment.getExternalStorageDirectory()}")

    // 获取系统目录/system
    LogTool.i("Environment#getRootDirectory: ${Environment.getRootDirectory()}")
}
```

## Context获取
```
fun filePath(context: Context) {
    // 获取app cache目录/data/user/0/com.dcxing.xxx/cache
    LogTool.i("Context#cacheDir: ${context.cacheDir.path}")

    // 获取app在sd卡cache目录/storage/emulated/0/Android/data/com.dcxing.xxx/cache
    LogTool.i("Context#externalCacheDir: ${context.externalCacheDir?.path}")

    // 获取app files目录/data/user/0/com.dcxing.xxx/files
    LogTool.i("Context#filesDir: ${context.filesDir.path}")

    // 获取app在sd卡obb目录/storage/emulated/0/Android/obb/com.dcxing.xxx
    LogTool.i("Context#obbDir: ${context.obbDir.path}")

    // 获取app 包名com.dcxing.xxx
    LogTool.i("Context#packageName: ${context.packageName}")

    // 获取app对应apk目录/data/app/~~Ni9J9RF1lRxYirZY5q_aEg==/com.dcxing.xxx-z==/base.apk
    LogTool.i("Context#packageCodePath: ${context.packageCodePath}")

    // 获取app安装包目录/data/app/~~Ni9J9RF1lRxYirZY5q_aEg==/com.dcxing.xxx-z==/base.apk
    LogTool.i("Context#packageResourcePath: ${context.packageResourcePath}")
}
```

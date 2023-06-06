# Kotlin

## 数据类序列化
1. build.gradle
```
plugins {
    id 'com.android.library'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-android'
    id 'kotlin-android-extensions'
}
android {
    ...
    androidExtensions {
        experimental = true
    }
}
```
2. kotlin
```
@Parcelize
data class Test(
    val id: Int = 1): Parcelable
```

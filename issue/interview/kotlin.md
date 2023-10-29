# Kotlin

## 数据类序列化
1. build.gradle
```
plugins {
    id 'com.android.library'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-android'
    id 'kotlin-parcelize'
}
android {
    ...
}
```
2. kotlin
```
import kotlinx.android.parcel.IgnoredOnParcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Test(val id: Int = 1): Parcelable {
    // 不序列化字段
    @IgnoredOnParcel var itemList: ArrayList<Xxx>? = null
}
```

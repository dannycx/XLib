# gradle问题

## build.gradle(:app) 属性介绍
```
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    ...
}

android {
    buildTypes {
        release {
            minifyEnabled true // 混淆
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            debuggable = true // 断点调试
        }
    }

    dataBinding { // 使用dataBinding
        enabled = true
    }
}

dependencies { // 依赖
    implementation fileTree(dir: 'libs', include: ['*.jar'])
}
```

## java.lang.ClassNotFoundException: Didn't find class "androidx.databinding.DataBinderMapperImpl"
* 依赖lib使用dataBinding，主module未使用，修改如下：
```
buildFeatures {
    dataBinding true
}
```

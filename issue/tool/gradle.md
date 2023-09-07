# gradle问题

## gradle signingReport生成SHA1
* 须保持gradle plugin 与 gradle版本对应，否则失败
* （4.2.2--7.2）
```
> Task :app:signingReport
Variant: debug
Config: debug
Store: C:\Users\xxx\.android\debug.keystore
Alias: AndroidDebugKey
MD5: F5:8F:8A:FA:B8:68:...
SHA1: B8:23:35:14:...
SHA-256: C2:6D:2B:F0:F1:95:0B:...
Valid until: 2053年8月10日星期日
```

## build.gradle(:app) 属性介绍
```
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    ...
}

def keystoreProps = new Properties()
keystoreProps.load(new FileInputStream(rootProject.file("keystore.properties")))

android {
    defaultConfig {
        applicationId "com.danny.xxx"
        minSdk 24
        targetSdk 33
        versionCode 1
        versionName "1.0.0.1"
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

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

    buildFeatures { // 默认不生成BuildConfig，修改如下即可
        buildConfig = true
    }

    applicationVariants.all{
        variant ->
            variant.outputs.all{
                outputFileName = "App-${variant.flavorName}-${variant.buildType.name}-${defaultConfig.versionName}.apk"
            }
    }

    signingConfigs {
        beta {
            storeFile file(keystoreProps['storeFile'])
            storePassword keystoreProps['storePassword']
            keyAlias keystoreProps['betaKeyAlias']
            keyPassword keystoreProps['betaKeyPassword']
        }
    }

    flavorDimensions "model", "chipset"
    productFlavors {
        xvn {
            dimension "model"
            versionNameSuffix ".xvn"
        }
        oem {
            dimension "model"
            versionNameSuffix ".oem"
        }
        beta {
            dimension "chipset"
            signingConfig signingConfigs.beta
        }
    }

    configurations {
        // 排除项目中的依赖，解决多版本依赖冲突问题
        // all*.exclude group: 'com.google.code.gson'
        // all*.exclude group: 'com.squareup.okhttp3'
        // all*.exclude group: 'com.squareup.okio'
    }
}

dependencies { // 依赖
    implementation fileTree(dir: 'libs', include: ['*.jar'])
}
```

## Algorithm HmacPBESHA256 not available
* 修改jdk版本至16及以上

## java.lang.ClassNotFoundException: Didn't find class "androidx.databinding.DataBinderMapperImpl"
* 依赖lib使用dataBinding，主module未使用，修改如下：
```
buildFeatures {
    dataBinding true
}
```

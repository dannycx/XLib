# gradle问题

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

# Android Studio详解

## gradle版本、sdk版本、jdk版本查看
***适配other项目***
1. 配置项目所需Gradle和SDK
2. 更改项目的Gradle和SDK以满足当前配置

### Gradle
1. Gradle版本：项目 -> gradle -> wrapper -> gradle-wrapper.properties
2. Gradle插件版本：项目 -> build.gradle -> id("com.android.application") version "8.2.0" apply false

### SDK
* 项目 -> app -> build.gradle -> android {}
* File -> Settings -> Android SDk

### JDK
* File -> Build,Execution,Deployment -> Gradle

## AS设置
* 背景：File -> Appearance & Behavior -> Appearance -> Background Image...

## TraceView数据采集和分析
- 单次执行耗时的方法
- 执行次数多的方法
1. Android Device Monitor启动
* sdk -> tools -> lib -> monitor-x86_64 -> monitor.exe

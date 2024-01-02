# ADB

## adb devices
```
C:\Users\xxx>adb devices -l
List of devices attached
10.100.94.165:5555     device product:velvet model:LGA311D2 device:velvet transport_id:4
```

## adb version
```
C:\Users\xxx>adb version
Android Debug Bridge version 1.0.41
Version 33.0.3-8952118
Installed as C:\Windows\adb.exe
```

## adb help
* 查看adb帮助文档

## adb connect 10.100.94.165
```
C:\Users\xxx>adb connect 10.100.94.165
connected to 10.100.94.165:5555
```

## adb disconnect 10.100.94.165
```
C:\Users\xxx>adb disconnect 10.100.94.165
disconnected 10.100.94.165
```

## adb disconnect == adb kill-server
* 断开所有连接设备

## adb start-server 或 adb kill-server
* 启动杀掉adb服务

## adb get-state
* 获取连接设备状态

## adb get-serialno
* 获取连接设备序列号

## adb get-devpath
* 获取连接设备路径

## adb reboot
* 重启
```
参数：
bootloader：系统加载器
recovery：恢复出厂设置
sideload：线刷模式
sideload-auto-reboot：
```

## adb root/unroot
* root/unroot权限

## adb remount
* 重新挂载设备，需root权限

## adb pull 设备路径.. 本地路径
* 从设备复制到电脑当前文件夹
```
adb pull /system/lib64/libwhiteboard_celerate.so .
adb pull /mnt/sdcard/Download/test.iwb .
```

## adb push 本地路径.. 设备路径
* 从电脑复制到设备文件夹
```
// 复制文件
adb push D:\test.txt /sdcard/test/
// 复制文件夹包含目录
adb push D:\test /sdcard/test/
// 复制文件夹不包含目录
adb push D:\test\. /sdcard/test/
```

## adb install d:\xxx.apk
* 安装app
```
参数：
-r(覆盖)
-d(删除)
-t(测试安装)
【AndroidManifest.xml -> application标签 -> android:testOnly="true"】【需加-t安装】
-g(授予所有运行时权限)
【AndroidManifest.xml -> manifest标签 -> android:sharedUserId="android.uid.system"】【系统应用才可以使用】
```

## adb uninstall com.xxx.xxx
* 卸载app
```
参数：
-k：保留应用数据（data/data）与缓存（/sdcard/Android/com.xxx.xxx）
adb shell pm uninstall --user 0 com.xxx.xxx
```

## adb keygen D:/test/a.key
* 生成adb公钥/私钥，a.key,a.key.pub

## adb shell pm list package -f | grep 关键字
* 查看应用

## 过度绘制adb命令
* adb -s 192.168.x.x shell setprop debug.hwui.overdraw show
* adb -s 192.168.x.x shell setprop debug.hwui.overdraw false

## 触摸点轨迹
* adb shell settings put system pointer_location 1

## 查看安装app版本信息
* adb shell pm dump com.xxx.xxx | grep "version"
* adb shell pm dump com.xxx.xxx | findstr version

## 查看触摸事件
* getevent -l -t | grep "BTN"

## 日志抓取
#### adb bugreport D:/test/
#### adb logcat | findstr 关键字 > E://logcat.txt
#### adb logcat *:E

**通过PID查看日志**
* 查看所有进程信息
> adb shell ps
* 查看指定关键字的进程信息（可为包名）
> adb shell "ps | grep com.xxx"
* 查看所有进程的log信息
> adb logcat -v process
* 查看指定PID的log信息
> adb logcat -v process | grep pid

**通过关键字直接查看**
* 查看所有的log日志
> adb logcat
* 过滤查看指定关键字的log日志
> adb logcat | grep com.xxx

**使用正则表达式匹配日志**
* 匹配"V/ActivityMAnager"字符串
> adb logcat | grep "^...Activity"

**其他日志**
* 清除日志缓存
> adb logcat -c
* 保存日志（保存到用户名下）
> adb logcat -v time > logcat.txt
* 保存到指定目录
> adb logcat -v time > d:\logcat.txt

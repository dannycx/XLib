# ADB

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
* adb logcat | findstr 关键字 > E://logcat.txt
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

## 卸载app
* adb shell pm uninstall --user 0 com.xxx.xxx

## 查看应用adb shell进入
* pm list package -f | grep 关键字

## 复制
1.从设备复制到电脑当前文件夹
* adb pull /system/lib64/libwhiteboard_celerate.so .
* adb pull /mnt/sdcard/Download/test.iwb .

2.从电脑复制到设备文件夹
* adb push test.txt /sdcard/test/

## 常用命令
* adb reboot 重启

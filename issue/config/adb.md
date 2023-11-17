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

# ADB

## 查看安装app版本信息
* adb shell pm dump com.xxx.xxx | grep "version"
* adb shell pm dump com.xxx.xxx | findstr version

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

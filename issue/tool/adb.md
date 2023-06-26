#ADB

## 查看安装app版本信息
* adb shell pm dump com.xxx.xxx | grep "version"
* adb shell pm dump com.xxx.xxx | findstr version

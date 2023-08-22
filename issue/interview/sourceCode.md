# 源码编译

## 虚拟机安装

## Ubuntu安装

## 源码下载
### 下载repo工具
1. 下载git
- sudo apt-get install git
> Error: unable to acquire the dpkg frontend lock(/var/lib/dpkg/lock-frontend)
  - sudo rm /var/lib/apt/lists/lock
  - sudo rm /var/cache/apt/archives/lock
  - sudo rm /var/lib/dpkg/lock*
  - sudo dpkg --configure -a
  
2. 创建bin，并加入PATH
- mkdir ~/bin
- PATH=~/bin:$PATH

3. 安装cur库
- sudo apt-get install curl 

4. 下载repo并设置权限
- curl https://mirrors.tuna.tsinghua.edu.cn/git/git-repo > ~/bin/repo
- chmod a+x ~/bin/repo

5. 安装python
- sudo apt-get install python3
> /usr/bin/env: "python":没有那个文件或目录
* sudo ln -s /usr/bin/python3.10 python 配置软连接

6. 软件更新
- sudo apt-get update 更新系统软件
- sudo apt-get upgrade 更新所有软件

7. 下载vim
- sudo apt-get install vim
* i 编辑
* ESC 普通模式
* : 命令模式（:wq保存并退出）
- source /etc/profile 环境变量立即生效无需重启

8. 安装jdk
- sudo apt-get install openjdk-11-jre-headless

9. 配置tuna镜像下载源码~/.bashrc
- export REPO_URL = 'https://mirrors.tuna.tsinghua.edu.cn/git/git-repo/'

10. 配置邮箱+名字
- git config --global user.email "xxx@xxx.com"
- git config --global user.name "xxx"


### 下载源码
* 建立工作目录并下载源码
  - mkdir aosp
  - cd aosp
  - repo init -u https://aosp.tuna.tsinghua.edu.cn/platform/manifest -b android-11.0.0_r1
  - repo sync


### 下载内核源码
* mkdir kernel
* cd kernel
* git clone https://aosp.tuna.tsinghua.edu.cn/kernel/goldfish.git   (模拟器)
  - cd goldfish
  - git branch -a
  - git checkout remotes/origin/android-goldfish-3.4


## 准备编译环境
1. 安装依赖包
```
sudo apt-get install git-core gnupg flex bison gperf build-essential zip curl zlib1g-dev gcc-multilib g++-multilib libc6-dev-i386 lib32ncurses5-dev x11proto-core-dev libx11-dev lib32z-dev ccache libgl1-mesa-dev libxml2-utils xsltproc unzip
```

## Android studio阅读源码
```
1. source build/envsetup.sh

2.lunch
     1. aosp_arm-eng
     2. aosp_arm64-eng
     3. aosp_mips-eng
     4. aosp_mips64-eng
     5. aosp_x86-eng
     6. aosp_x86_64-eng
     ...
     55. hikey960-userdebug
BUILDTYPE 指的是编译类型，有以下三种：
user：用来正式发布到市场的版本，权限受限，如没有 root 权限，不能 dedug，adb默认处于停用状态。
userdebug：在user版本的基础上开放了 root 权限和 debug 权限，adb默认处于启用状态。一般用于调试真机。
eng：开发工程师的版本，拥有最大的权限(root等)，具有额外调试工具的开发配置。一般用于模拟器。

这里选择6：lunch 6 或 lunch aosp_x86_64-eng

3.make -j4
最终会在 out/target/product/generic_x86/目录生成了三个重要的镜像文件： system.img、userdata.img、ramdisk.img。大概介绍着三个镜像文件：
system.img：系统镜像，里面包含了Android系统主要的目录和文件，通过init.c进行解析并mount挂载到/system目录下。
userdata.img：用户镜像，是Android系统中存放用户数据的，通过init.c进行解析并mount挂载到/data目录下。
ramdisk.img：根文件系统镜像，包含一些启动Android系统的重要文件，比如init.rc。
```


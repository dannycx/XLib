# 源码编译

## 虚拟机安装

## Ubuntu安装

## 源码下载
### 下载repo工具
1. 下载git
- sudo apt-get install git
> Error: unable to acquire the dpkg frontend lock(/var/lib/dpkg/lock-frontend)
  - sudo rm /var/cache/apt/archives/lock
- sudo rm /var/lib/dpkg/lock
  
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

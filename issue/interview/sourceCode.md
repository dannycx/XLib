# 源码编译

## 虚拟机安装

## Ubuntu安装

## 源码下载
### 准备工作
1. 下载repo工具
   * 下载git
- sudo apt-get install git
> Error: unable to acquire the dpkg frontend lock(/var/lib/dpkg/lock-frontend)
- sudo rm /var/cache/apt/archives/lock
- sudo rm /var/lib/dpkg/lock
  
   * 创建bin，并加入PATH
- mkdir ~/bin
- PATH=~/bin:$PATH

   * 安装cur库
- sudo apt-get install curl 

   * 下载repo并设置权限
- curl https://mirrors.tuna.tsinghua.edu.cn/git/git-repo > ~/bin/repo
- chmod a+x ~/bin/repo

   * 安装python
- sudo apt-get install python3
> /usr/bin/env: "python":没有那个文件或目录
* sudo ln -s /usr/bin/python3.10 python 配置软连接


   * 软件更新
- sudo apt-get update 更新系统软件
- sudo apt-get upgrade 更新所有软件

   * 下载vim
- sudo apt-get install vim
  * i 编辑
  * ESC 普通模式
  * : 命令模式（:wq保存并退出）
- source /etc/profile 环境变量立即生效无需重启

   * 安装jdk
- sudo apt-get install openjdk-11-jre-headless

2. 下载源码














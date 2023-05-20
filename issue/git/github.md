# github

## github命令
+ git add . 添加文件
+ git commit -m "提交文件"
+ git push 合并至master

+ git config --global -l 查看当前配置
+ git config --global --unset http.proxy 取消代理
+ git remote rm origin 之前仓报错，删除一下
+ git remote add origin https://github.com/X 关联自己github项目
+ git config --global http.sslVerify false 取消SSL校验
+ git config --global user.name "dannycx" 用户名
+ git config --global user.email "xxx@xx.com" 邮箱

## github SSH
1. 生成密钥
> ssh -keygen -t rsa -C "xxx@xx.com"
2. 找到密钥文件：c:\用户\xx\.ssh
> 复制id_rsa文件内容
3. 登录github添加SSH key
> github/settings

## github创建Android项目

1. 登录github新建项目
2. Android Studio新建项目
3. Git Bash进入项目根目录
4. git clone https://github.com/dannycx/XXX.git
5. 项目根目录下出现XXX文件夹，进入XXX，ls -al查看所有文件，将所有文件复制到项目根目录，同名文件直接覆盖（注意：.git为隐藏文件，也需复制）
6. 删除根目录下的XXX文件夹
7. 完活
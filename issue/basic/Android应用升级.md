# Android应用升级

## 适配点
1. 避免Android存储卡权限
* 使用内部cache目录，避免涉及存储卡权限
2. Android N FileProvider适配
* 应用安装涉及文件uri传递
3. Android O对应用安装进行权限限制
* 引入安装权限
4. Android P对http网络请求约束
* Android P默认不允许直接使用http请求，需用https

## App更新封装
[App更新]()

# Activity篇之Intent全解
## 显示启动
* 明确指定组件信息来完成调用
```
startActivity(Intent(this, XxxActivity::class.java))
```

## 隐式启动
* 匹配目标组件的IntentFilter完成调用
* IntentFilter：action、category、data
* 可有多组IntentFilter，匹配一组即可完成跳转
```
// 解决找不到对应组件方式
1. Intent#resolveActivity(packageManager)查找是否存在组件
2. PackageManager#resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)查找是否存在组件
```

1. action匹配规则
* 字符串（字母区分大小写）
* 一个IntentFilter中可有多个action，但必须有一个action
* 多个action时匹配一个即可

2. category匹配规则
* 字符串
* 一个IntentFilter中可以没有
* 若有不管几个，每个都要属于一组IntentFilter中定义的（即intent携带category要全部包含在一组IntentFliter中的category）
> 例：intent中携带3个，activity中定义5个，携带的3个必须是5个里面的3个
* startActivity时默认会添加android.intent.category.DEFAULT

3. data匹配规则
* 两部分组成mimeType + URI

**MimeType**
* MimeType指资源类型(表示不同数据格式)：文本、图片、音视频等
```
// 获取mimeType
fun mimeType(filePath: String): String {
    val ext = MimeTypeMap.getFileExtensionFromUrl(filePath)
    return MimeTypeMap.singleton.getMimeTypeFromExtension(ext)
}
```
```
// 音视频
<data android:mimeType="video/*"/>
<data android:mimeType="audio/*"/>
// 图片
<data android:mimeType="image/*"/>
// 文本
<data android:mimeType="text/*"/>
// pdf
<data android:mimeType="application/pdf"/>
// prf
<data android:mimeType="application/pics-rules"/>
// pot pos ppt
<data android:mimeType="application/vnd.ms-powerpoint"/>
// potx
<data android:mimeType="application/vnd.openxmlformats-officedocument.presentationml.template"/>
// pptx
<data android:mimeType="application/vnd.openxmlformats-officedocument.presentationml.presentation"/>
// ppsx
<data android:mimeType="application/vnd.openxmlformats-officedocument.presentationml.slideshow"/>
// xls
<data android:mimeType="application/vnd.ms-excel"/>
// xlsx
<data android:mimeType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
// xltx
<data android:mimeType="application/vnd.openxmlformats-officedocument.spreadsheetml.template"/>
// doc、dot
<data android:mimeType="application/msword"/>
// docx
<data android:mimeType="application/vnd.openxmlformats-officedocument.wordprocessingml.document"/>
// dotx
<data android:mimeType="application/vnd.openxmlformats-officedocument.wordprocessingml.template"/>
// js
<data android:mimeType="application/x-javascript"/>
// zip
<data android:mimeType="application/zip"/>
<data android:mimeType="application/x-zip-compressed"/>
// rar
<data android:mimeType="application/rar"/>
<data android:mimeType="application/x-rar-compressed"/>
// jar
<data android:mimeType="application/ava-archive"/>
// tar
<data android:mimeType="application/x-tar"/>
// tgz
<data android:mimeType="application/x-compressed"/>
// *、bin、class dms、exe等
<data android:mimeType="application/octet-stream"/>
```

**URI**
* 一个IntentFilter可有多个data
* 若包含data，intent中必须含有data，并且只要匹配其中一个即可
* 结构：scheme://host:port/(path|pathPrefix|pathPattern)
```
<data
    android:scheme="string"
    android:host="string"
    android:port="8080"
    android:path="/string"
    android:pathPattern="string"
    android:pathPrefix="/string"
    android:mimeType="string" />

scheme：URI格式，例：http、content、file等，未指定整个URI无效
host：主机名，例：www.baidu.com，未指定整个URI无效
port：端口号，指定scheme和host时才有效
path：完整路径信息
pathPattern：完整路径信息，可包含通配符"",'^'表示0或多个任意字符
pathPrefix：路径前缀信息
```
* 调用方式
```
// 同时存在MimeType和URI，不可分开设置type和data，分开会导致其一为null
intent.setDataAndType(Uri.parse(""), "image/*")
```

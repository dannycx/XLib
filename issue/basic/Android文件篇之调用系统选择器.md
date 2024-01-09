# Android文件篇之调用系统选择器
## 打开系统app资源选择器选取图片、文件方式
** Intent.ACTION_PICK：图片 **
* "android.intent.action.PICK"
* intent.data获取资源
* 资源类型"content://"开头的Uri资源
* 可通过context.contentResolver获取资源内容相关信息
```
fun pickFile(context: Context) {
    val intent = Intent(Intent.ACTION_PICK)
    // "video/*"、"*/*"
    intent.type = "image/*"
    context.startActivityForResult(intent, REQUEST_CODE_PICK)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    data?:return

    when (requestCode) {
        REQUEST_CODE_PICK -> {
            // uri资源仅在当前activity实例未销毁前访问，activity销毁后会报无权限uri资源错误
            val uri = data.data

            // 获取图片流
            val imageStream = contentResolver.openInputStream(uri)

            // 查询图片详细信息
            val cursor = contentResolver.query(uri, null, null, null, null)
        }
    }
}
```

** Intent.ACTION_GET_CONTENT：文件 **


** Intent.ACTION_OPEN_DOCUMENT：文件 **


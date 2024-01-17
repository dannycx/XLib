# View常见问题

## view布局设置点击事件，点击时导致其与其他浮窗有阴影
* android:clickable="true"
* 改为设置touch事件
```
mBinding.layout.setOnTouchListener { _, _ -> true }
```

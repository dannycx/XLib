# Paint

## 字体样式设置
```
val paint = Paint()
paint.isAntiAlias = true // 抗锯齿
paint.isDither = true // 抖动，更平滑
paint.color = textColor // 颜色

paint.style = Paint.Style.STROKE // 样式
paint.strokeCap = Paint.Cap.ROUND // 笔触风格，半圆形
paint.strokeJoin = Paint.Join.ROUND // 连接点结合方式，圆弧
paint.strokeMiter = 2f // 画笔倾斜度，大于0，图形锐角样式

// 文本样式
paint.textSize = textSize // 字体大小

// 以基准点居中对齐
paint.textAlign = Paint.Align.CENTER

// 地理位置
paint.textLocale = Locale.getDefault()

// 水平方向缩放因子，大于1拉伸宽度，小于1压缩宽度
paint.textScaleX = 2f

paint.letterSpacing = 3f // 行间距，默认0，负值压缩行间距
paint.fontFeatureSettings = "CSS样式"
paint.isLinearText = true // 线性文本标识，true不需要文本缓存
paint.isSubpixelText = true // 亚像素，文字更清晰，有助于LCD屏幕显示
paint.hinting = Paint.HINTING_ON // 画笔隐藏模式

// 斜体，负数右斜，正数左斜
paint.textSkewX = if (isItalic) -0.25f else 0f

// 粗体
paint.isFakeBoldText = isBold
paint.typeface = Typeface.DEFAULT_BOLD // 字体类型名称
val font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD) // 字体风格
paint.typeface = font

// 删除线
paint.isStrikeThruText = isStrikethrough

// 下划线
paint.isUnderlineText = isUnderlineText
```

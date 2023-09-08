# Paint

## 字体样式设置
```
val paint = Paint()
paint.color = textColor // 颜色
paint.textSize = textSize // 字体大小

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

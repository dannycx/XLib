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

**验证码绘制**
```
val bitmap = Bitmap.createBitmap(300, 180, Bitmap.Config.ARGB_8888)
val canvas = Canvas(bitmap)
canvas.drawColor(Color.WHITE)

val paint = Paint()
paint.isAntiAlias = true
paint.color = Color.BLACK
paint.style = Paint.Style.FILL
// 边框
canvas.drawRect(Rect(40, 20, 260, 160), paint)

// 验证码
val text = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"]

val random = Random()
val randomText = CharArray(4)
int index = 0
while (index < 4 && randomText[index] == 0) {
	val charTemp = text[random.nextInt(text.length)]
	val contains = false
	for (i in 0 until randomText.length) {
		if (charTemp == randomText[i]) {
			contains = true
			break;
		}
	}
	if (!contains) {
		randomText[index] = charTemp
	}
}

paint.textSize = 30f
paint.isFakeboldText = true
paint.letterSpacing = 1
canvas.drawText(randomText, 0, 4, 50, 40, paint)

paint.style = Paint.Style.STROKE
for (i in 0 until 100) {
	// 随机颜色
	val color = Color.argb(150, 55 + random.nextInt(200), 55 + random.nextInt(200), 55 + random.nextInt(200))
	paint.color = color

	// 直线
	// val startY = 40 + random.nextInt(100)
	// val endX = 40 + random.nextInt(100)
	// canvas.drawLine(40, startY, endX, startY, paint)

	// 贝塞尔
	val path = Path()
	val controlX = 40 + random.nextInt(100)
	val controlY = 20 + random.nextInt(100)
	path.moveTo(40, 100 + random.nextInt(100))

	val endX = 260
	val endY = 20 + random.nextInt(100)
	path.quadTo(controlX, controlY, endX, endY)
	canvas.drawPath(path, paint)
}
```






# Android绘制

**View的invalidate**
```
// UI线程调用
// 重绘整个布局
fun invalidate()
// 重绘指定区域
fun invalidate(l: Int, t: Int, r: Int, b: Int)
fun invalidate(rect: Rect)

// 子线程调用
fun postInvalidate()
fun postInvalidate(l: Int, t: Int, r: Int, b: Int)
```
* ViewRoot.scheduleTraversals() -> 异步消息 -> performTraversals() -> onDraw()
* 调用invalidate()相当于调用onDraw()
* onDraw()实现绘制逻辑
* 实现刷新组件或动画可通过调用invalidate()实现（改变绘制数据并重绘）。
```
// 实现动画
val radius = 60
var centerX = radius
var centerY = radius

// 移动方向，右下为正
var moveX = true
var moveY = true

val paint = Paint()
paint.color = Color.YELLOW
paint.style = Paint.Style.FILL

fun onDraw(canvas: Canvas) {
	canvas.drawColor(Color.RED)
	canvas.drawCircle(centerX, centerY, radius, paint)

	val maxWidth = measuredWidth
	val maxHeight = measuredHeight
	if (centerX < radius) {
		moveX = true
	} else if (centerX > maxWidth - radius) {
		moveX = false
	}
	centerX = if (moveX) centerX + 12 else centerX - 12

	if (centerY < radius) {
		moveY = true
	} else if (centerY > maxHeight - radius) {
		moveY = false
	}
	centerY = if (moveY) centerY + 6 else centerY - 6
}
```

**坐标转换**
* 坐标默认左上角(0, 0)为原点，右下为正，左上为负，可通过canvas方法对坐标转换（改变原点坐标）：平移、旋转、缩放和拉斜
---
+ 使用注意：使用前后要恢复原始状态，否则后续绘制功能都会按新新坐标绘制
+ fun save(): Int
+ fun restore()
---
```
// 平移，改变原点坐标，水平垂直移动dx，dy距离，新的原点为平移后坐标
fun translate(dx: Float, dy: Float)
例：
canvas.drawPoint(100, 100, paint)
等价于
canvas.translate(100, 100)
canvas.drawPoint(0, 0, paint)


// 旋转，可指定旋转中心（默认原点），正：顺时针，负：逆时针
fun rotate(degree: Float)
fun rotate(degree: Float, px: Float, py: Float)


// 缩放，可指定缩放中心（默认原点），sx，sy为缩放比例
fun scale(sx: Float, sy: FLoat)
fun scale(sx: Float, sy: Float, px: Float, py: FLoat)


// 拉斜，sx，sy为x，y方向倾斜角的tan值
fun skew(sx: Float, sy: Float)
例：y轴倾斜45°，则tan45=1，则canvas.skew(0, 1)


案例：绘制时钟
canvas.save()
canvas.translate(100, 100) // 留边
canvas.drawCircle(60, 60, 60, paint) // 表盘
for (i in 0 until 12) {
	canvas.drawLine(0, 60, 10, 60, paint) // 刻度，9点钟刻度
	canvas.rotate(30, 60, 60) // 以圆心旋转，绘制钟表每个刻度，刻度间距 = 360 / 12
}
canvas.restore()
```

**canvas中使用Matrix矩阵**
* Matrix是3*3矩阵，同样可直线坐标转换
```
// 平移，改变原点坐标，水平垂直移动dx，dy距离，新的原点为平移后坐标
fun setTranslate(dx: Float, dy: Float)

// 旋转，可指定旋转中心（默认原点），正：顺时针，负：逆时针
fun setRotate(degree: Float)
fun setRotate(degree: Float, px: Float, py: Float)

// 缩放，可指定缩放中心（默认原点），sx，sy为缩放比例
fun setScale(sx: Float, sy: FLoat)
fun setScale(sx: Float, sy: Float, px: Float, py: FLoat)

// 拉斜，sx，sy为x，y方向倾斜角的tan值
fun setSkew(sx: Float, sy: Float)
fun setSkew(sx: Float, sy: Float, px: Float, py: FLoat)

例：
val matrix = Matrix()
matrix.setTranslate(100, 100)
canvas.matrix = matrix
```

**裁剪区Clip**

![](https://github.com/dannycx/XLib/blob/main/issue/draw/clip.png)
![](/clip.png)





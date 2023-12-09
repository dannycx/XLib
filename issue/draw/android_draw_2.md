# Android绘制

**缓存技术**

### 双缓存技术
* 两个绘图区：bitmap的canvas、当前view的canvas
* 先将图形绘制在bitmap上，再将bitmap绘制在view上
* view上看到的效果其实是bitmap上的内容
#### 优点
1. 高绘图性能：先绘制在bitmap，在绘制view上，可提高绘图性能
2. 可在屏幕展示绘图过程：绘制椭圆跟随着手指变化大小，抬手只留最后一个椭圆
3. 保存绘制历史：可将绘制历史结果保存至bitmap

### Path绘制曲线
#### 优点
1. path可实时保存绘图坐标，可避免invalidate()重绘时因ViewRoot.scheduleTraversals()异步出现的问题
2. path可绘制复杂图形
3. 绘图效率更高

### 双缓存 + path绘制曲线
```
lateinit var cacheBitmap: Bitmap
lateinit var bitmapCanvas: Canvas
var preX = 0f
var preY = 0f
val path = Path()
val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
	color = Color.BLACK
	style = Paint.Style.STROKE
	strokeJoint = Paint.Join.ROUND
	strokeCap = Paint.Cap.ROUND
	strokeWidth = 3f
}

@Override
fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
	super.onSizeChanged(w, h, oldW, oldH)
	if (!::cacheBitmap.isInitialized) {
	    cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
	    bitmapCanvas = Canvas(cacheBitmap)
	}
}

@Override
fun onDraw(canvas: Canvas?) {
	canvas?:return
	canvas.drawColor(Color.WHITE)
	// 历史path
	canvas.drawBitmap(cacheBitmap, 0, 0, null)
	// 当前path
	canvas.drawPath(path, paint)
}

@Override
fun onTouchEvent(event: MotionEvent?) {
	event?:return false

	val x = event.x
	val y = event.y

	when (event.action) {
	    MotionEvent.ACTION_DOWN -> {
	        path.reset()
	        preX = x
	        preY = y
	        path.move(x, y)
	    }

	    MotionEvent.ACTION_MOVE -> {
	        val controlX = (preX + x) / 2
	        val controlY = (preY + y) / 2
	        path.quadTo(controlX, controlY, x, y)
	        invalidate()
	        preX = x
	        preY = y
	    }

	    MotionEvent.ACTION_UP -> {
	        bitmapCanvas.drawPath(path, paint)
	        invalidate()
	    }
	}
	return true
}
```

### 双缓存 + path绘制圆
```
lateinit var cacheBitmap: Bitmap
lateinit var bitmapCanvas: Canvas
var firstX = 0f
var firstY = 0f
val path = Path()
val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
	color = Color.BLACK
	style = Paint.Style.STROKE
	strokeJoint = Paint.Join.ROUND
	strokeCap = Paint.Cap.ROUND
	strokeWidth = 3f
}

@Override
fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
	super.onSizeChanged(w, h, oldW, oldH)
	if (!::cacheBitmap.isInitialized) {
	    cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
	    bitmapCanvas = Canvas(cacheBitmap)
	}
}

@Override
fun onDraw(canvas: Canvas?) {
	canvas?:return
	canvas.drawColor(Color.WHITE)
	// 历史path
	canvas.drawBitmap(cacheBitmap, 0, 0, null)
	// 当前path
	canvas.drawPath(path, paint)
}

@Override
fun onTouchEvent(event: MotionEvent?) {
	event?:return false

	val x = event.x
	val y = event.y

	when (event.action) {
	    MotionEvent.ACTION_DOWN -> {
	        path.reset()
	        firstX = x
	        firstY = y
	    }

	    MotionEvent.ACTION_MOVE -> {
	        path.reset()
	        val circleW = x - firstX
	        val circleH = y - firstY
	        val centerX = firstX + circleW / 2
	        val centerY = firstY + circleH / 2
	        val radius = sqrt(circleW * circleW + circleH * circleH)
	        path.addCircle(centerX, centerX, radius, Path.Direction.CCW)
	        invalidate()
	    }

	    MotionEvent.ACTION_UP -> {
	        bitmapCanvas.drawPath(path, paint)
	        invalidate()
	    }
	}
	return true
}
```



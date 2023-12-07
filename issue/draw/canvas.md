# Android绘图

**Canvas画布**
* 提供绘制各种图案方法：点、线、圆等。

**Paint画笔**
* 提供画笔各种属性：颜色、文本、图形样式、位图模式、滤镜等
[Paint属性](https://github.com/dannycx/XLib/blob/main/issue/draw/paint.md)

**path路径**

* 路径属性：直，曲，闭合，非闭合，圆，方等
[Path属性](https://github.com/dannycx/XLib/blob/main/issue/draw/path.md)

## Canvas
**绘制位图**
* 如需进行位图计算需传paint，否则可传null
```
// 绘制bitmap指定右上角坐标，大小与原图相同，不进行任何缩放
void draw(bitmap: Bitmap, left: Float, top: FLoat, paint: Paint)
// 从源bitmap中抠出scr区域图片绘制到canvas的dst处，src与dst大小影响绘制效果，自动缩放以适应dst区域
void draw(bitmap: Bitmap, src: Rect, dst: Rect, paint: Paint)
void draw(bitmap: Bitmap, src: Rect, dst: Rect, paint: Paint)
```

**绘制点**
* 点大小取决于paint.strokeWidth，值越大点越大
```
// x,y处绘制一点
void drawPoint(x: Float, y: Float, paint: Paint)
// 从pts中取值，每2个值构成一个点，连续绘制多个点，pts长度为奇数忽略最后一点
void drawPoints(pts: FloatArray, paint: Paint)
// 从pts中offset位置取值，取count个点，每2个值构成一个点，连续绘制多个点，忽略最后单点值
void drawPoints(pts: FloatArray, offset: Int, count: Int, paint: Paint)
```

**绘制直线**
* 两点确定一直线
```
// 开始结束点绘制直线
void drawLine(startX: FLoat, startY: FLoat, stopX: FLoat, stopY: Float, paint: Paint)
// 从pts中取值，每4个值构成一条直线，多余忽略
void drawLines(pts: FloatArray, paint: Paint)
// 从pts中offset位置取值，取count个点，每4个值构成一条直线，忽略多余
void drawLines(pts: FloatArray, offset: Int, count: Int, paint: Paint)
```

**绘制矩形**
```
// 直角矩形
void drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint)
void drawRect(rect: Rect, paint: Paint)
void drawRect(rectF: RectF, paint: Paint)

// 圆角矩形
// rx,ry为圆角水平垂直半径
void drawRoundRect(left: Float, top: Float, right: Float, bottom: Float, rx: FLoat, ry: Float, paint: Paint)
void drawRect(rectF: RectF, rx: FLoat, ry: Float, paint: Paint)
```

**绘制圆**
```
// 椭圆
void drawOval(left: Float, top: Float, right: Float, bottom: Float, paint: Paint)
void drawOval(oval: RectF, paint: Paint)

// 圆：圆心（cx, cy），半径radius
void drawCircle(cx: Float, cy: Float, radius: Float, paint: Paint)

// 弧线，扇形
// 范围：oval，起始角：startAngle，弧线、扇形角度：sweepAngle，useCenter：true扇形，false弧线
// 角度正负数表示顺逆时针方向
void drawArc(oval: RectF, startAngle: Float, sweepAngle: Float, useCenter: Boolean, paint: Paint)
void drawArc(left: Float, top: Float, right: Float, bottom: Float, startAngle: Float, sweepAngle: Float, useCenter: Boolean, paint: Paint)
```

**绘制路径**
#### 往 Path 添加线条
* 可绘制奇形怪状线条，并可将线条组合在一起变为折线，闭合成多边形
```
// 移动画笔至（x, y）绝对定位
void moveTo(x: Float, y: Float)
// 移动至新点，新点在上一点基础偏移dx，dy相对定位
void rMoveTo(dx: Float, dy: FLoat)
// 连接到（x,y）与上一点连接
void lineTo(x: Float, y: Float)
// 连接到新点，新点在上一点基础上偏移dx，dy
void rLineTo(dx: Float, dy: Float)
// 闭合，连接第一个点和最后一个点
void close()
```

#### 往 Path 中添加矩形、椭圆、弧
* Path.Direction：CW顺时针，CCW逆时针
```
// 添加矩形
void addRect(rect: RectF, dir: Path.Direction)
void addRect(left: Float, top: Float, right: Float, bottom: Float, dir: Path.Direction)

// 添加圆角矩形：可定义四个角弧线大小
void addRoundRect(rect: Rect, radii: FloatArray, dir: Path.Direction)
void addRoundRect(rect: Rect, rx: Float, ry: Float, dir: Path.Direction)
void addRoundRect(left: Float, top: Float, right: Float, bottom: Float, radii: FloatArray, dir: Path.Direction)

// 添加椭圆
void addOval(oval: RectF, dir: Path.Direction)
void addOval(left: Float, top: Float, right: Float, bottom: Float, dir: Path.Direction)

// 添加圆
void addCircle(x: Float, y: Float, radius: Float, dir: Path.Direction)

// 添加弧
void addArc(oval: RectF, startAngle: Float, sweepAngle: Float)
void addArc(left: Float, top: Float, right: Float, bottom: Float, startAngle: Float, sweepAngle: Float)
```

#### 往 Path 中添加曲线和贝塞尔曲线
* 二阶贝塞尔曲线：3点确定一条平滑曲线（起点、控制点、终点）
* 三阶贝塞尔曲线：4点确定一条平滑曲线（起点、控制点、控制点、终点）
```
// （x1,y1）控制点，（x2,y2）终点
void quadTo(x1: Float, y1: Float, x2: Float, y2: Float)
void rQuadTo(dx1: Float, dy1: Float, dx2: Float, dy2: Float)
// （x1,y1）、（x2,y2）控制点，（x3,y3）终点
void cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float)
void rCubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float)

// 添加曲线
// forceMoveTo为true表示开始一个新图形不与上一点连接，false与上一点连接
void arcTo(oval: RectF, startAngle: Float, sweepAngle: Float, forceMoveTo: Boolean)
void arcTo(oval: RectF, startAngle: Float, sweepAngle: Float)
void arcTo(left: Float, top: Float, right: Float, bottom: Float, startAngle: Float, sweepAngle: Float, forceMoveTo: Boolean)
```

#### Path 中图形计算
* 多个Path间可进行图形运算，得到更复杂和不规则图形
```
    Path.Op.DIFFERENCE：差集，A减与B重叠区域余下区域
    Path.Op.INTERSECT：交集，A与B重叠区域
    Path.Op.REVERSE_DIFFERENCE：反差集，B减与A重叠区域余下区域
    Path.Op.UNION：并集，A与B并集
    Path.Op.XOR：补集，并集 - 交集
```

**绘制文字**
```
// 指定位置开始绘制文字
void drawText(text: CharArray, index: Int, count: Int, x: Float, y: FLoat, paint: Paint)
void drawText(text: String, x: Float, y: FLoat, paint: Paint)
void drawText(text: String, start: Int, end: Int, x: Float, y: FLoat, paint: Paint)
void drawText(text: CharSequence, start: Int, end: Int, x: Float, y: FLoat, paint: Paint)

// 沿path绘制文字，hOffset、vOffset与path水平垂直偏移距离
void drawTextOnPath(text: String, path: Path, hOffset: Float, vOffset: Float, paint: Paint)
void drawTextOnPath(text: CharArray, index: Int, count: Int, path: Path, hOffset: Float, vOffset: Float, paint: Paint)

// 指定位置绘制文字（已过时）,按自定义坐标集绘制文字
void drawPosText(text: String, @Size(multiple = 2) pos: FloatArray, paint: Paint)
void drawPosText(text: CharArray, index: Int, count: Int, @Size(multiple = 2) pos: FloatArray, paint: Paint)
例：实现垂直文本可传坐标集
val paint = Paint()
val text = "好好学习"
val array = FloatArray(text.length * 2).apply {
	for (i in 0 until size step 2) {
	    set(i, 100f) // x,水平方向每个字x相同，都是100
	    set(i + 1, (i + 1) * paint.textSize) // y,垂直方向每个字间隔一个字大小
	}
}
canvas.drawPosText(text, array, paint)
```

**Paint的FontMetrics使用**
```
FontMetrics属性：
base line：基准线y=0
top：上边界，值为负值，值等于距base line距离
ascent：负值，值等于距base line距离
descent：正值，值等于距base line距离
bottom：正值，值等于距base line距离
leading：两行间距，上一行bottom与下一行top间距，值总为0，可忽略
```

#### 行距
* 相邻两行的基线之间距离。默认等于descent.abs() + ascent.abs()
* 可通过属性android:lineSpacingExtra和android:lineSpacingMultiplier修改行距
* lineSpacingExtra默认为0
```
行距 = 默认行距 * lineSpacingMultiplier + lineSpacingExtra
```

#### fontPadding计算
* 顶fontPadding = (top - ascent).abs()
* 底fontPadding = bottom - descent
* 通过android:includeFontPadding可决定字体高度是否包含fontPadding
```
其他（Word，Stetch等）高度为descent.abs() + ascent.abs()
androoid中字体高度为bottom.abs() + top.abs()
所以android字体垂直方向比设计稿多占空间，可通过设置android:includeFontPadding=false解决
```

#### 文本在控件水平垂直居中显示
```
-------------top----------------
-------------ascent-------------
-------------文字正中------------
-------------baseline-----------
-------------descent------------
-------------bottom-------------
```
* x = (view.width - text.length) / 2
* 文字高度 = fontMetrics.bottom - fontMetrics.top
* 文字正中 = 文字高度 / 2
* 文字正中到baseline = 文字高度一半 - fontMetrics.bottom
* val distanceY = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
* y = view.height / 2 + distanceY
```
// 位于控件水平垂直位置
canvas.drawText(text, x, y, paint)
```

#### Paint的setTextAlign使用
* 控制文字水平对齐方式（文字与起点相对位子）
* Align.LEFT：居左，起点在文字左侧
* Align.CENTER：居中，起点在文字中间
* Align.RIGHT：居右，起点在文字右侧

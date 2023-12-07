# Android绘图

**Canvas画布**
* 提供绘制各种图案方法：点、线、圆等。

**Paint画笔**
* 提供画笔各种属性：颜色、文本、图形样式、位图模式、滤镜等
[Paint属性](https://github.com/dannycx/XLib/issue/draw/paint.md)

**path路径**

* 路径属性：直，曲，闭合，非闭合，圆，方等
[Path属性](https://github.com/dannycx/XLib/issue/draw/path.md)

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
//-

    Path.Op.DIFFERENCE:










# Android绘图

## 阴影Shader
* 层（layer）：主层（文字图形层）、阴影层（主层下添加新层）
* 阴影属性：模糊度、偏移量、颜色
```
# Paint.class
// 阴影半径（0无阴影效果）、x，y方向偏移量（小发光效果）、阴影颜色
fun setShadowLayer(radius: Float, dx: Float, dy: Float, shadowColor: Int)

# View.class
// 阴影类型：View.LAYER_TYPE_SOFTWARE、View.LAYER_TYPE_HARDWARE
// 默认View.LAYER_TYPE_HARDWARE
// View.LAYER_TYPE_SOFTWARE该类型下才会有阴影效果
fun setLayerType(layerType: Int, paint: Paint)

// 清除阴影
fun clearShadowLayer()

// 添加阴影和发光效果
class TestView(...): View(...) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
	    isAntiAlias = true
	    textSize = 100
	    setShadowLayer(10, 1, 1, Color.YELLOW)
    }

    init {
        // 启用阴影，paint可传null，否则使用paint地方都有阴影效果
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint)
    }

    @Override
    fun onDraw(canvas: Canvas?) {
        canvas?:return

        canvas.drawText("阴影文本", 100, 100, paint)
        paint.setShadowLayer(10, 5, 5, Color.RED)
        canvas.drawText("新阴影效果文本"， 100， 200， paint)
    }
}
```


## 渐变Gradient
* 绘制过程中颜色和位图以特定规律变化，以增强物体质感和审美
**渐变种类**
* LinearGradient：线性渐变（颜色渐变）
* RadialGradient：径向渐变（颜色渐变）
* SweepGradient：扫描渐变（颜色渐变）
* BitmapGradient：位图渐变（壁纸平铺）
* ComposeGradient：混合渐变（组合渐变）
**渐变模式**
* TileMode.CLAMP：超出规定区域重复边缘的效果
* TileMode.MIRROR：镜像方式显示
* TileMode.REPEAT：在竖直水平方向上重复
```
# Paint.class
// 设置渐变效果
fun setShader(shader: Shader)
```
**线性渐变LinearGradient**
* 按渐变方向填充绘图区域
```
// 起点：(x0, y0)终点：(x1, y1)，起始颜色：color0，color1，渐变模式
LinearGradient(x0: Float, y0: Float, x1: Float, y1: Float, color0: Int, color1: Int, tile: TileMode)
// 起点：(x0, y0)终点：(x1, y1)，颜色：colors，颜色起始比例：positions，渐变模式
LinearGradient(x0: Float, y0: Float, x1: Float, y1: Float, colors: IntArray, positions: FloatArray, tile: TileMode)

例：渐变圆环
class GradientCircleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, def: Int = 0): View(context, attrs, def) {
    private var outRadius = ResTool.getDimension(context, R.dimen.dp_20)
    private var innerRadius = ResTool.getDimension(context, R.dimen.dp_18)
    private var border = ResTool.getDimension(context, R.dimen.dp_4)
    private val outPath = Path()

    private var showBorder = true
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        ctrokeCap = PAint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val rectF = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        rect.set(0f, 0f, w.toFloat(), h.toFloat())

        paint.shader = LinearGradient(rectF.left, rectF.top, rectF.right, rectF.bottom, intArrayOf(Color.RED, Color.Blue, Color.YELLOW, Color.WHITE), floatArrayOf(0f, 0.25f, 0.75f, 1f), shader.TileMode.CLAMP)

        val innerPath = Path()
        outPath.addCircle(w / 2f, h / 2f, outRadius, Path.Direction.CCW)
        rectF.inset(border, border)
    }
}
```

**RadialGradient：径向渐变**
* 以指定点为中心向四周渐变
```
// 中心点：(x, y)，渐变半径：radius，起始颜色：color0，color1，渐变模式
RadialGradient(x: Float, y: Float, radius: Float, color0: Int, color1: Int, tile: TileMode)
// 中心点：(x, y)，渐变半径：radius，颜色：colors，颜色起始比例：positions，渐变模式
RadialGradient(x: Float, y: Float, radius: Float, colors: IntArray, positions: FloatArray, tile: TileMode)
```

## 位图运算












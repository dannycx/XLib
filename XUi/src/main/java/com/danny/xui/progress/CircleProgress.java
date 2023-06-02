package com.danny.xui.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.x.xui.R;


/**
 * @description 倒计时进度条
 * @author danny
 * @date 2017/11/20.
 */
public class CircleProgress extends View {
    /** 画笔对象的引用 */
    private Paint paint;
    /** 圆环的颜色 */
    private int roundColor;
    /** 圆环进度的颜色 */
    private int roundProgressColor;
    /** 中间进度百分比的字符串的颜色 */
    private int textColor;
    /** 中间进度百分比的字符串的字体 */
    private float textSize;
    /**  圆环的宽度 */
    private float roundWidth;
    /** 最大进度 */
    private int max;
    /** 当前进度 */
    private float progress;
    /** 是否显示中间的进度 */
    private boolean textIsDisplayable;
    /** 进度的风格，实心或者空心 */
    private int style;
    /** 画弧形的形状 */
    private RectF oval;

    /** strokeCap */
    private int strokeCap;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();

        oval = new RectF(0, 0, 0, 0);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CircleProgress);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.CircleProgress_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.CircleProgress_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.CircleProgress_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.CircleProgress_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.CircleProgress_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.CircleProgress_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.CircleProgress_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.CircleProgress_style, 0);
        strokeCap = mTypedArray.getInt(R.styleable.CircleProgress_strokeCap,0);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /** 画最外层的大圆环 */
        //获取圆心的x坐标
        int centre = getWidth() / 2;
        //圆环的半径
        int radius = (int) (centre - roundWidth / 2);
        //设置圆环的颜色
        paint.setColor(roundColor);
        //设置空心
        paint.setStyle(Paint.Style.STROKE);
        //设置圆环的宽度
        paint.setStrokeWidth(roundWidth);
        //消除锯齿
        paint.setAntiAlias(true);
        //画出圆环
        canvas.drawCircle(centre, centre, radius, paint);

        /** 画进度百分比 */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        //设置字体
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        int percent = (int) ((progress / (float) max) * 100);
        //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        float textWidth = paint.measureText((100 - percent) / 10 + "S");

        if (textIsDisplayable && percent != 0 && style == STROKE) {
            //画出进度百分比
            canvas.drawText((100 - percent) / 10 + "S", centre - textWidth / 2, centre + textSize / 2, paint);
        }

        /** 画圆弧 ，画圆环的进度 */
        //设置圆环的宽度
        paint.setStrokeWidth(roundWidth);
        //设置笔触形状
        paint.setColor(roundProgressColor);
        //用于定义的圆弧的形状和大小的界限
        oval.set(centre - radius, centre - radius, centre
                + radius, centre + radius);

        //环形顶端的形状
        switch (strokeCap) {
            case 0:
                paint.setStrokeCap(Paint.Cap.BUTT);
                break;
            case 1:
                paint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case 2:
                paint.setStrokeCap(Paint.Cap.SQUARE);
                break;
            default:
                break;
        }

        switch (style) {
            case STROKE: {
                //根据进度画圆弧
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90, -(360 - 360 * progress / max), false, paint);
                break;
            }
            case FILL: {
                //根据进度画圆弧
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0) {
                    canvas.drawArc(oval, -90, -(360 - 360 * progress / max), true, paint);
                }
                break;
            }
            default:
                break;
        }
    }


    public int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     */
    public void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     */
    public synchronized float getProgress() {
        return progress;
    }

    /**
     * 设置进度
     */
    public synchronized void setProgress(float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

}

package com.danny.xui.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.danny.xui.R;

/**
 * 结尾带圆点的环形进度条,结合圆形图像使用
 *
 * @author danny
 * @since  2018-10-25
 */
public class RingProgress extends View {
    private int height, width;
    private Paint paint;
    private int upPaintWidth = dp2px(2);
    private int downPaintWidth = dp2px(2);
    private int padding = 0;
    private
    @ColorInt
    int upPaintColor, downPaintColor;
    private RectF centerRectF;
    private int startAngle = 270;// 开始角度
    private int maxValue = 100;// 最大值
    private int progressValue = 0;// 进度
    private boolean isInverse = false;// 是否是逆时针
    private
    @ColorInt
    int pointColor = Color.BLUE;
    private int pointWidth = dp2px(2.5f);
    private boolean isScrolling = false;
    private Handler handler;
    private int sweepAngle = 0;

    public RingProgress(Context context) {
        this(context, null);
    }

    public RingProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                postInvalidate();
            }
        };
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true); //启用抗锯齿
        paint.setDither(true); //启用抗颜色抖动（可以让渐变更平缓）
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RingProgress);

        upPaintWidth = array.getDimensionPixelSize(R.styleable.RingProgress_upPaintWidth, dp2px(2));
        downPaintWidth = array.getDimensionPixelSize(R.styleable.RingProgress_downPaintWidth, dp2px(2));

        upPaintColor = array.getColor(R.styleable.RingProgress_upPaintColor, Color.BLUE);
        downPaintColor = array.getColor(R.styleable.RingProgress_downPaintColor, Color.parseColor("#FFF1F5F7"));

        padding = array.getDimensionPixelSize(R.styleable.RingProgress_circlePadding, 0);

        startAngle = array.getInt(R.styleable.RingProgress_startAngle, 270);
        maxValue = array.getInt(R.styleable.RingProgress_maxValue, 100);
        progressValue = array.getInt(R.styleable.RingProgress_progressValue, 0);

        pointColor = array.getColor(R.styleable.RingProgress_pointColor, Color.BLUE);
        pointWidth = array.getDimensionPixelSize(R.styleable.RingProgress_pointWidth,dp2px(2.5f));

        isInverse = array.getBoolean(R.styleable.RingProgress_isInverse, false);
        array.recycle();
        sweepAngle = (int) ((float) progressValue / maxValue * 360);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        centerRectF = getCenterRectF(width, height, downPaintWidth, padding);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    private RectF getCenterRectF(int width, int height, int paintWidth, int padding) {
        paintWidth = paintWidth / 2;
        RectF rect = null;
        if (width < height) {
            rect = new RectF(0 + padding + paintWidth, height / 2 - width / 2 + paintWidth + padding, width - paintWidth - padding, height / 2 + width / 2 - paintWidth - padding);
        } else if (height < width) {
            rect = new RectF(width / 2 - height / 2 + paintWidth + padding, 0 + paintWidth + padding, width / 2 + height / 2 - paintWidth - padding, height - paintWidth - padding);
        } else {
            rect = new RectF(0 + padding + paintWidth, 0 + paintWidth + padding, width - paintWidth - padding, height - paintWidth - padding);
        }
        return rect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 底色
        paint.setColor(downPaintColor);
        paint.setStrokeWidth(downPaintWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(centerRectF, startAngle, 360, false, paint);

        // 进度的弧度
        paint.setColor(upPaintColor);
        paint.setStrokeWidth(upPaintWidth);
        canvas.drawArc(centerRectF, startAngle, isInverse ? -sweepAngle : sweepAngle, false, paint);

        // 开始的圆点的顶端
        int r = Math.min(width, height) / 2 - padding - downPaintWidth / 2;
//        int startX = (int) (width / 2 + r * Math.cos((startAngle) * Math.PI / 180));
//        int startY = (int) (height / 2 + r * Math.sin((startAngle) * Math.PI / 180));
//        paint.setColor(pointColor);
//        paint.setStrokeWidth(upPaintWidth / 2);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(startX, startY, upPaintWidth / 2, paint);

        // 末尾的圆点
        int endX = (int) (width / 2 + r * Math.cos((startAngle + (isInverse ? -sweepAngle : sweepAngle)) * Math.PI / 180));
        int endY = (int) (height / 2 + r * Math.sin((startAngle + (isInverse ? -sweepAngle : sweepAngle)) * Math.PI / 180));
        paint.setColor(pointColor);
        paint.setStrokeWidth(pointWidth);
        paint.setStyle(Paint.Style.FILL);
        if (progressValue != 0) {
            canvas.drawCircle(endX, endY, pointWidth, paint);
        }

        super.onDraw(canvas);
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        sweepAngle = (int) ((float) progressValue / maxValue * 360);
        handler.sendEmptyMessage(0);
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
        sweepAngle = (int) ((float) progressValue / maxValue * 360);
        handler.sendEmptyMessage(0);
    }

    /**
     * 自动滚动
     *
     * @param time
     */
    public void autoScrllo(final long time) {
        if (isScrolling || progressValue == 0) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                isScrolling = true;
                int oldSweepAngle = sweepAngle;
                //int sweepAngle = (int) ((float) progressValue / maxValue * 360);
                for (int i = 0; i <= oldSweepAngle; i++) {
                    try {
                        Thread.sleep(time/oldSweepAngle);
                        sweepAngle = i;
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isScrolling = false;
            }
        }.start();
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return 像素值
     */
    public int dp2px(float dp) {
        // 方式一
//        float density = context.getResources().getDisplayMetrics().density;
//        return (int) (dp * density + 0.5f);
        // 方式二
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }
}

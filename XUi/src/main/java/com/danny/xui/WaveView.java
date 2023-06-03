package com.danny.xui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.danny.xtool.BitmapTool;

/**
 * 水波纹
 */
public class WaveView extends View {
    private Paint paint;
    private Path path;
    private int dx;
    private int dy;
    private Bitmap bitmap;
    private int width;
    private int height;
    private Region region;
    private ValueAnimator animator;

    // 属性
    private int bitmapId = 0;
    private int waveHeight = 200;
    private int waveLength = 400;
    private boolean rise = false;
    private int originY = 500;
    private int duration = 2000;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
            rise = ta.getBoolean(R.styleable.WaveView_rise, false);
            waveLength = ta.getDimensionPixelSize(R.styleable.WaveView_wave_length, 400);
            waveHeight = ta.getDimensionPixelSize(R.styleable.WaveView_wave_height, 200);
            originY = ta.getDimensionPixelSize(R.styleable.WaveView_originY, 500);
            duration = ta.getDimensionPixelSize(R.styleable.WaveView_duration, 2000);
            bitmapId = ta.getResourceId(R.styleable.WaveView_boat_bitmap, 0);
            ta.recycle();
        }

        // 加载图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;// 缩放图片
        if (bitmapId > 0) {
            bitmap = BitmapFactory.decodeResource(getResources(), bitmapId, options);
            bitmap = BitmapTool.INSTANCE.createCircleBitmap(bitmap);
        } else {
//            bitmap = BitmapFactory.decodeResource(getResources(), , options);// 默认图片
        }

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);// 填充

        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
        width = widthSize;
        height = heightSize;
        if (originY == 0) {
            originY = height;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 定义曲线
        setPathData();
        canvas.drawPath(path, paint);

        // 绘制头像
        PathMeasure pm = new PathMeasure(path, false);
//        pm.getLength();路径长度
        Rect bounds = region.getBounds();
        if (bounds.top > 0 || bounds.right > 0) {
            if (bounds.top < originY) {// 波峰到基准线
                canvas.drawBitmap(bitmap, bounds.right - bitmap.getWidth() / 2, bounds.top - bitmap.getHeight(), paint);
            } else {
                canvas.drawBitmap(bitmap, bounds.right - bitmap.getWidth() / 2, bounds.bottom - bitmap.getHeight(), paint);
            }
        } else {
            float x = width / 2 - bitmap.getWidth() / 2;
            canvas.drawBitmap(bitmap, x, originY - bitmap.getHeight(), paint);
        }

    }

    private void setPathData() {
        path.reset();
        int halfWaveLength = waveLength / 2;
        path.moveTo(-waveLength + dx, originY/* - dy*/);
        for (int i = -waveLength; i < width + waveLength; i += waveLength) {
//            path.quadTo();
            path.rQuadTo(halfWaveLength / 2, -waveHeight, halfWaveLength, 0);// 相对坐标
            path.rQuadTo(halfWaveLength / 2, waveHeight, halfWaveLength, 0);// 相对坐标
        }

        // 切割矩形
        region = new Region();
        Region clip = new Region((int) (width / 2 - 0.1f), 0, width / 2, height * 2);
        region.setPath(path, clip);

        // 曲线封闭
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
    }

    public void startAnimation() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());// 解决动画每执行一次卡顿问题
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (float) animation.getAnimatedValue();
                dx = (int) (waveLength * factor);// 不断移动初始点位置
                dy += 2;// 涨水效果
                postInvalidate();
            }
        });
        animator.start();
    }
}

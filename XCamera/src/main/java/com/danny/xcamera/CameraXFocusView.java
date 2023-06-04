package com.danny.xcamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.Nullable;

import com.danny.xtool.UiTool;

/**
 * 焦点聚焦
 *
 * @author danny
 * @since 2020-11-02
 */
public class CameraXFocusView extends View {
    private long DELAY_TIME = 300;
    private int touchX = Integer.MIN_VALUE;
    private int touchY = Integer.MIN_VALUE;

    private int displayWidth;
    private int displayHeight;

    /**
     * 边框长度
     */
    private int rectWidth;

    /**
     * 边框宽内部长度
     */
    private float rectLineWidth;

    /**
     * 焦点框边框宽度
     */
    private float borderWidth;

    /**
     * 焦点框边框圆角
     */
    private float borderRadio;

    private Paint paint;
    private Animation animator;
    private Handler handler;


    public CameraXFocusView(Context context) {
        this(context, null);
    }

    public CameraXFocusView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraXFocusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handler = new Handler();

        displayWidth = UiTool.INSTANCE.displayWidth(context);
        displayHeight = UiTool.INSTANCE.displayHeight(context);
        rectWidth = UiTool.INSTANCE.dimensionPixelSize(context, com.danny.common.R.dimen.dp_70);
        borderWidth = UiTool.INSTANCE.dimensionPixelSize(context, com.danny.common.R.dimen.dp_0_75);
        borderRadio = UiTool.INSTANCE.dimensionPixelSize(context, com.danny.common.R.dimen.dp_3_5);
        rectLineWidth = UiTool.INSTANCE.dimensionPixelSize(context, com.danny.common.R.dimen.dp_5);

        initPaint();
    }

    public void startFocus(float touchX, float touchY) {
        this.touchX = (int) touchX;
        this.touchY = (int) touchY;
        setVisibility(VISIBLE);
        startAnimation(animator);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> setVisibility(GONE), DELAY_TIME);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(borderWidth);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (touchX != Integer.MIN_VALUE && touchY != Integer.MIN_VALUE) {
            int rectHalfWidth = (int) (rectWidth / 2);
            int left = Math.min(0, touchX - rectHalfWidth);
            int top = Math.min(0, touchY - rectHalfWidth);
            int right = Math.min(displayWidth, touchX + rectHalfWidth);
            int bottom = Math.min(displayHeight, touchY + rectHalfWidth);

            int realWidth = Math.min(right - left, bottom - top);

            if (realWidth < rectWidth) {
                realWidth = rectWidth;
            }

            if (right == displayWidth) {
                left = displayWidth - realWidth;
            }

            if (bottom == displayHeight) {
                top = displayHeight - realWidth;
            }

            canvas.drawRoundRect(left, top, left + realWidth, top + realWidth, borderRadio, borderRadio, paint);

            int realHalfWidth = realWidth / 2;
            canvas.drawLine(left, top + realHalfWidth, left + rectLineWidth, top + realHalfWidth, paint);
            canvas.drawLine(left + realHalfWidth, top, left + realHalfWidth, top + rectLineWidth, paint);
            canvas.drawLine(right - rectLineWidth, bottom - realHalfWidth, right, bottom - realHalfWidth, paint);
            canvas.drawLine(right - realHalfWidth, bottom - rectLineWidth, right - realHalfWidth, bottom, paint);

            super.onDraw(canvas);
        }
    }
}

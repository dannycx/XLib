package com.danny.xui.gesture;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.x.xui.R;

/**
 * @author danny
 * @date 2018/9/26
 *
 * @description 手势密码提示view
 */

public class GestureTipView extends View {
    private int row = 3;
    private int col = 3;
    private int width = 60;
    private int height = 60;
    private int f = 5;
    private int g = 5;
    private int strokeWidth = 28;

    private Paint paint = null;
    private Drawable normal = null;
    private Drawable pressed = null;
    private String password;

    public GestureTipView(Context context) {
        super(context);
    }

    public GestureTipView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);

        normal = getResources().getDrawable(R.drawable.icon_gesture_tip_normal);
        pressed = getResources().getDrawable(R.drawable.icon_gesture_tip_pressed);
        if (pressed != null) {
            width = pressed.getIntrinsicWidth() + 8;
            height = pressed.getIntrinsicHeight() + 8;
            f = width / 4;
            g = height / 4;
            normal.setBounds(0, 0, width / 2, height / 2);
            pressed.setBounds(0, 0, width / 2, height / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pressed == null || normal == null) {
            return;
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                paint.setColor(16777216);
                int i1 = j * height + j * g;
                int i2 = i * width + i * f;
                canvas.save();
                canvas.translate(i1, i2);
                String num = String.valueOf(col * i + (j + 1));
                if (!TextUtils.isEmpty(password)) {
                    if (password.indexOf(num) == -1) {
                        normal.draw(canvas);// 绘制正常的点
                    }else {
                        pressed.draw(canvas);// 密码中存在该点绘制
                    }
                }else {
                    normal.draw(canvas);// 绘制正常的点
                }
                canvas.restore();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (pressed != null) {
            setMeasuredDimension(col * height + g * (col - 1), row * width + f * (row - 1));
        }
    }

    /**
     * 设置密码，重绘提示
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
        invalidate();
    }
}

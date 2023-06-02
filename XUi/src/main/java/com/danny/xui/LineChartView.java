package com.danny.xui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


import com.x.xui.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义view-折线图
 * Created by danny on 2018/8/23.
 */

public class LineChartView extends View {
    private static final String TAG = LineChartView.class.getSimpleName();
    private static int[] DEFAULT_GRADIENT_COLORS = {Color.BLUE, Color.GREEN};
    private Paint txtPaint;
    private Paint dotPaint;
    private Paint linePaint;
    private Paint gradientPaint;
    private Rect txtRect;
    private Path path;
    private Path gradientPath;
    
    private int[] datas;
    private int max;
    private String[] txts;
    private final int radius;
    private final int clickRadius;
    private List<Dot> dots = new ArrayList<>();
    private int gap;
    private int step;
    private int selectedDotIndex = -1;
    
    private int selectedDotColor;
    private int normalDotColor;
    private int lineColor;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);
        lineColor = typedArray.getColor(R.styleable.LineChartView_line_color, Color.BLACK);
        normalDotColor = typedArray.getColor(R.styleable.LineChartView_dot_normal_color, Color.BLACK);
        selectedDotColor = typedArray.getColor(R.styleable.LineChartView_dot_selected_color, Color.RED);
        typedArray.recycle();
        initPaint();
        path = new Path();
        gradientPath = new Path();
        radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        clickRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        txtRect = new Rect();
        gap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
    }

    private void initPaint() {
        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);
        txtPaint.setTextSize(20);
        txtPaint.setTextAlign(Paint.Align.CENTER);
        
        dotPaint = new Paint();
        dotPaint.setAntiAlias(true);
        
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(3);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        gradientPaint = new Paint();
        gradientPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        dots.clear();
        int width = w - getPaddingLeft() - getPaddingRight();
        int height = h - getPaddingTop() - getPaddingBottom();
        step = width / (datas.length - 1);
        txtPaint.getTextBounds(txts[0], 0, txts[0].length(), txtRect);
        int barHeight = height - txtRect.height() - gap;
        float heightRatio = barHeight / max;
        for (int i = 0; i < datas.length; i++) {
            Dot dot = new Dot();
            dot.value = datas[i];
            dot.transformedValue = (int) (dot.value * heightRatio);
            dot.x = step * i + getPaddingLeft();
            dot.y = getPaddingTop() + barHeight - dot.transformedValue;
            if (i == 0) {
                path.moveTo(dot.x, dot.y);
                gradientPath.moveTo(dot.x, dot.y);
            } else {
                path.lineTo(dot.x, dot.y);
                gradientPath.lineTo(dot.x, dot.y);
            }
            if (i == datas.length - 1) {
                int bottom = getPaddingTop() + barHeight;
                gradientPath.lineTo(dot.x, bottom);
                Dot firstDot = dots.get(0);
                gradientPath.lineTo(firstDot.x, bottom);
                gradientPath.lineTo(firstDot.x, firstDot.y);
            }
            dots.add(dot);
        }
        Shader shader = new LinearGradient(0, 0, 0, getHeight(), DEFAULT_GRADIENT_COLORS, null, Shader.TileMode.CLAMP);
        gradientPaint.setShader(shader);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, linePaint);
        canvas.drawPath(gradientPath, gradientPaint);
        for (int i = 0; i < dots.size(); i++) {
            String axis = txts[i];
            int x = getPaddingLeft() + i * step;
            int y = getHeight()-getPaddingBottom();
            canvas.drawText(axis, x, y, txtPaint);
            Dot dot = dots.get(i);
            if (i == selectedDotIndex) {
                dotPaint.setColor(selectedDotColor);
                canvas.drawText(String.valueOf(datas[i]), dot.x, dot.y - radius - gap, txtPaint);
            } else {
                dotPaint.setColor(normalDotColor);
            }
            canvas.drawCircle(dot.x, dot.y, radius, dotPaint);
        }
    }

    public void setDatas(int[] dataList, int max) {
        datas = dataList;
        this.max= max;
    }

    public void setTxt(String[] horizontalAxis) {
        txts = horizontalAxis;
    }

    /**
     * 拐点属性
     */
    private class Dot {
        int x;
        int y;
        int value;
        int transformedValue;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                selectedDotIndex = getClickDotIndex(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                selectedDotIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    private int getClickDotIndex(float x, float y) {
        int index = -1;
        for (int i = 0; i < dots.size(); i++) {
            Dot dot = dots.get(i);
            int left = dot.x - clickRadius;
            int top = dot.y - clickRadius;
            int right = dot.x + clickRadius;
            int bottom = dot.y + clickRadius;
            if (x > left && x < right && y > top && y < bottom) {
                index = i;
                break;
            }
        }
        return index;
    }
}

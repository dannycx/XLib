package com.danny.xui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义view-柱状图
 * Created by danny on 2018/8/22.
 */
public class RectChatView extends View {
    private static final String TAG = RectChatView.class.getSimpleName();
    private static final int DELAY = 10;
    private static final int BAR_GROW_STEP = 15;//柱状条增长动画步长
    
    private Paint barPaint;
    private Paint txtPaint;

    private float[] datas;//柱状图数据列表
    private String[] txts;//水平方向x轴坐标
    private List<Bar> bars = new ArrayList<>();
    
    private float barWidth;
    private float max;//数据集合的最大值
    private int gap;//坐标文本与柱状条之间间隔的变量
    private int radius;

    private int selectedIndex = -1;
    private boolean enableGrowAnimation = true;

    private Rect txtRect;
    private RectF rectF;

    public RectChatView(Context context) {
        this(context, null);
    }
    
    public RectChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();//初始化方法
    }
    
    private void init() {
        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);
        txtPaint.setTextSize(20);
        txtPaint.setTextAlign(Paint.Align.CENTER);
        
        barPaint = new Paint();
        barPaint.setColor(Color.BLUE);
        barPaint.setAntiAlias(true);

        txtRect = new Rect();
        rectF = new RectF();
        
        //柱状条宽度 默认8dp
        barWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        //柱状条与坐标文本之间的间隔大小，默认8dp
        gap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bars.clear();//清空柱状条Bar的集合
        
        //去除padding，计算绘制所有柱状条所占的宽和高
        int width = w - getPaddingLeft() - getPaddingRight();
        int height = h - getPaddingTop() - getPaddingBottom();

        //按照数据集合的大小平分宽度
        int step = width / datas.length;
        //barWidth为柱状条宽度的变量，可以设置，radius为柱状条宽度的一半
        radius = (int) (barWidth / 2);
        
        //计算第一条柱状条的左边位置
        int barLeft = getPaddingLeft() + step / 2 - radius;
        //通过坐标文本画笔计算绘制x轴第一个坐标文本占据的矩形边界，这里主要获取其高度，为计算maxBarHeight提供数据
        txtPaint.getTextBounds(txts[0], 0, txts[0].length(), txtRect);
        
        //计算柱状条高度的最大像素大小，txtRect.height为底部x轴坐标文本的高度，gap为坐标文本与柱状条之间间隔大小的变量
        int maxBarHeight = height - txtRect.height() - gap;
        
        //计算柱状条最大像素大小与最大数据值的比值
        float heightRatio = maxBarHeight / max;
        
        //遍历数据集合，初始化所有的柱状条Bar对象
        for (float data : datas) {
            Bar bar = new Bar();//创建柱状条对象
            bar.value = data;//设置原始数据
            bar.transformedValue = bar.value * heightRatio;//计算原始数据对应的像素高度大小
            //计算绘制柱状条的四个位置
            bar.left = barLeft;
            bar.top = (int) (getPaddingTop() + maxBarHeight - bar.transformedValue);
            bar.right = (int) (barLeft + barWidth);
            bar.bottom = getPaddingTop() + maxBarHeight;
            
            //初始化绘制柱状条时当前的top值，用作动画
            bar.currentTop = bar.bottom;
            
            //将初始化好的Bar添加到集合中
            bars.add(bar);
            //更新柱状条左边位置，为初始化下一个Bar对象做准备
            barLeft += step;
        }
    }
    
    /**
     * 绘制柱状图
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (enableGrowAnimation) {
            drawBarsWidthAnimation(canvas);
        } else {
            drawBars(canvas);
        }
    }

    /**
     * 绘制柱状条，带自增长动画
     */
    private void drawBarsWidthAnimation(Canvas canvas) {
        //遍历所有的Bar
        for (int i = 0; i < datas.length; i++) {
            Bar bar = bars.get(i);
            //绘制坐标文本
            String axis = txts[i];
            float textX = bar.left + radius;
            float textY = getHeight() - getPaddingBottom();
            canvas.drawText(axis, textX, textY, txtPaint);
            //更新当前柱状条顶部位置变量，BAR_GROW_STEP为柱状条增长的步长，即让柱状条长高BAR_GROW_STEP长度
            bar.currentTop -= BAR_GROW_STEP;
            //当计算出来的currentTop小于柱状条本来的top值时，说明越界了
            if (bar.currentTop <= bar.top) {
                //将currentTop重置成本来的top值，解决越界问题
                bar.currentTop = bar.top;
                //高度最高的柱状条的顶部位置为paddingTop，如果currentTop等于paddingTop，说明高度最高的进度条也到达
                //其最高点，可以停止增长动画了，于是将enableGrowAnimation置为false
                if (bar.currentTop == getPaddingTop()) {
                    enableGrowAnimation = false;
                }
            }
            //绘制圆角柱状条
            rectF.set(bar.left, bar.currentTop, bar.right, bar.bottom);
            canvas.drawRoundRect(rectF, radius, radius, barPaint);
        }
        //延时触发重新绘制，调用onDraw方法
        if (enableGrowAnimation) {
            postInvalidateDelayed(DELAY);
        }
    }

    /**
     * 绘制柱状条，没有动画效果的模式
     */
    private void drawBars(Canvas canvas) {
        //遍历所有的Bar对象，一个个绘制
        for (int i = 0; i < bars.size(); i++) {
            Bar bar = bars.get(i);
            //绘制底部x轴坐标文本
            String axis = txts[i];//获取对应位置的坐标文本
            //计算绘制文本的起始位置坐标(textX，textY)，textX为柱状条的中线位置，由于我们对画笔txtPaint设置了
            //Paint.Align.CENTER，所以绘制出来的文本的中线与柱状条的中线是重合的
            float textX = bar.left + radius;
            float textY = getHeight() - getPaddingBottom();
            //绘制坐标文本
            canvas.drawText(axis, textX, textY, txtPaint);
            if (i == selectedIndex) {
                barPaint.setColor(Color.RED);
                float x = bar.left + radius;
                float y = bar.top - gap;
                canvas.drawText(String.valueOf(bar.value), x, y, txtPaint);
            } else {
                barPaint.setColor(Color.BLUE);
            }
            //设置柱状条矩形
            rectF.set(bar.left, bar.top, bar.right, bar.bottom);
            //绘制圆角矩形
            canvas.drawRoundRect(rectF, radius, radius, barPaint);
            //绘制直角矩形 //canvas.drawRect(rectF, barPaint);
        }
    }
    
    /**
     * 设置水平方向x轴坐标值
     *
     * @param txts 坐标值数组，如{"1", "2", "3", "4","5", "6", "7", "8", "9"}
     */
    public void setTxt(String[] txts) {
        this.txts = txts;
    }
    
    /**
     * 设置柱状图数据
     *
     * @param dataList 数据数组，如{15, 54, 45, 26, 89, 70, 59, 28, 10}
     * @param max 数据数组中的最大值，如89，最大值用来计算绘制时的高度比例
     */
    public void setDatas(float[] dataList, int max) {
        datas = dataList;
        this.max = max;
    }

    /**
     * 记录柱状图属性
     */
    private class Bar {
        //绘制柱状条的四个位置
        int left;
        int top;
        int right;
        int bottom;
        float value;//柱状条原始数据的大小
        float transformedValue;//柱状条原始数据大小转换成对应的像素大小
        int currentTop;//柱状图动画中用到，表示柱状条动画过程中顶部位置的变量，取值范围为[0,top]
        
        boolean isInside(float x, float y) {
            return x > left && x < right && y > top && y < bottom;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enableGrowAnimation) {
            return false;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < bars.size(); i++) {
                    if (bars.get(i).isInside(event.getX(), event.getY())) {
                        enableGrowAnimation = false;
                        selectedIndex = i;
                        invalidate();
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                selectedIndex = -1;
                enableGrowAnimation = false;
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 设置选择位置
     *
     * @param selectedIndex 位置
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * 设置是否启动增长动画
     *
     * @param enableGrowAnimation true-启动
     */
    public void setEnableGrowAnimation(boolean enableGrowAnimation) {
        this.enableGrowAnimation = enableGrowAnimation;
    }
}

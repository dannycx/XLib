package com.danny.xui.trend;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.x.xui.R;

import java.math.BigDecimal;

/**
 * Created by hj on 2018/11/23.
 * 说明：漏斗View,可自定义宽度，颜色，高度，描述文字，线宽，线颜色等...
 * 详细操作请阅读README.md
 * github: https://github.com/Jay-huangjie/FunnelView
 */
public class FunnelView extends View {
    private static final String TAG = "FunnelView";

    private FunnelData funnelData;// 数据源
    private Paint paint = null;// 梯形画笔
    private Paint labelPaint = null;// 描述画笔
    private Paint shadowPaint = null;// 阴影画笔
    private Paint titlePaint = null;// 标题画笔

    private float right = 0.0f;
    private float bottom = 0.0f;
    private Context context;
    private float lastLineOffset; //最底部从中心点向两边的偏移量
    private float itemHeight; //单个梯形的目标高度
    private int count; //漏斗的个数
    private float plotBottom; //底部坐标
    private boolean EXACTLY; //是否是精确高度

    /*
     * 中心点坐标，绘制是从下往上，这个坐标是最底部那跟线的中心点
     * 最后一根线的长度= lastLineOffset*2
     * */
    private float centerX;
    private float plotHeight;// 图表总高度
    private float plotWidth;// 图表总宽度
    private float mTopMaxLineHalf;// 最长的线的宽度的一半
    private int labelColor;// 描述文字颜色
    private float labelSize;// 描述文字大小
    private int titleColor;// 标题颜色
    private float titleSize;// 标题大小
    private int shadowColor;// 阴影颜色

    public FunnelView(Context context) {
        super(context);
        initView(context, null);
    }

    public FunnelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FunnelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        this.context = context;
        if (attributeSet != null) {
            TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.FunnelView);
            lastLineOffset = ta.getDimension(R.styleable.FunnelView_lastLineOffset, dip2px(context, 15));
            itemHeight = ta.getDimension(R.styleable.FunnelView_itemHeight, dip2px(context, 20));
            labelColor = ta.getColor(R.styleable.FunnelView_labelColor, Color.WHITE);
            labelSize = ta.getDimension(R.styleable.FunnelView_labelSize, sp2px(context, 12));
            titleColor = ta.getColor(R.styleable.FunnelView_labelColor, Color.BLACK);
            titleSize = ta.getDimension(R.styleable.FunnelView_labelSize, sp2px(context, 14));
            shadowColor = ta.getColor(R.styleable.FunnelView_shadowColor, Color.parseColor("#1A000000"));
            ta.recycle();
        }
        disableHardwareAccelerated(this);
        chartRender();
    }

    private void chartRender() {
        // 梯形上文字画笔
        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextAlign(Paint.Align.LEFT);
        labelPaint.setColor(labelColor);
        labelPaint.setTextSize(labelSize);

        // 梯形
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 阴影
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(shadowColor);

        // 标题
        titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(titleColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        //图所占范围大小
        right = w;
        bottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            canvas.save();
            calcPlotRange();
            renderPlot(canvas);
            canvas.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderPlot(Canvas canvas) {
        if (null == funnelData) {
            Log.e(TAG, "FunnelView=>未设置数据源!");
            return;
        }

        float funnelHeight = EXACTLY ? plotHeight / count : itemHeight;
        float cx = centerX;
        renderPlotDesc(canvas, cx, funnelHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 100;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { //wrap_content
            result = Math.min(result, specSize);
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = (int) ((int) (itemHeight * count + 30 * (count - 1)) + dip2px(context, 15));
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
            result = specSize;
            EXACTLY = true;
        }
        return result;
    }

    /**
     * 计算一些关键数据
     */
    private void calcPlotRange() {
        plotBottom = bottom - getPaddingBottom();
        float mPlotTop = getPaddingTop();
        float mPlotLeft = getPaddingLeft();
        float mPlotRight = right - getPaddingRight();
        plotWidth = Math.abs(mPlotRight - mPlotLeft);
        plotHeight = Math.abs(plotBottom - mPlotTop);
//        centerX = (int) mTopMaxLineHalf + mPlotLeft;
        // 居中
        centerX = right / 2;
    }

    /**
     * 绘制梯形
     *
     * @param canvas       画布
     * @param cx           梯形中心坐标
     * @param funnelHeight 梯形的高度
     */
    private void renderPlotDesc(Canvas canvas, float cx, float funnelHeight) {
        int count = funnelData.funnels.size();
        float halfWidth = 0.f; //梯形的半径
        float bottomY;
        PointF pStart = new PointF();
        PointF pStop = new PointF();
        pStart.x = cx - plotWidth / 2;
        pStop.x = cx + plotWidth / 2;
        pStart.y = pStop.y = plotBottom;
        float lineY;
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            FunnelData.Funnel d = funnelData.funnels.get(i);
            path.reset();
            if (i == 0) { //画底部的线，从左下角开始绘制
                path.moveTo(cx - lastLineOffset, plotBottom - dip2px(context, 7));
                //底部线默认长度为80
                path.lineTo(cx + lastLineOffset, plotBottom - dip2px(context, 7));
            } else {
                path.moveTo(pStart.x, pStart.y - dip2px(context, 7));
                path.lineTo(pStop.x, pStop.y - dip2px(context, 7));
            }

            float lastX = pStart.x;
            float lastY = pStart.y;
            //根据数量来调整倾斜角度,如果需要实现别的倾斜效果只需实现HalfWidthCallback接口
            halfWidth += getDefaultHalfWidthOffset();

            bottomY = sub(plotBottom, i * funnelHeight);
            lineY = bottomY - funnelHeight / 2;
            pStart.x = cx - lastLineOffset - halfWidth;
            pStart.y = bottomY - funnelHeight;
            pStop.x = cx + lastLineOffset + halfWidth;
            pStop.y = bottomY - funnelHeight;

            path.lineTo(pStop.x, pStop.y); // 画梯形右边的线
            path.lineTo(pStart.x, pStart.y); // 画梯形左边的线
            paint.setColor(d.color);
            path.close();
            canvas.drawPath(path, paint);

            if (i != 0) { // 绘制中间的阴影间隔
                Path shadowPath = new Path();
                shadowPath.moveTo(lastX, lastY);
                shadowPath.lineTo(cx + dip2px(context, 5), lastY);
                shadowPath.lineTo(pStop.x - getDefaultHalfWidthOffset(), lastY - dip2px(context, 7));
                shadowPath.lineTo(cx - dip2px(context, 5), lastY - dip2px(context, 7));
                shadowPath.close();
                canvas.drawPath(shadowPath, shadowPaint);
            }

            // 绘制描述文字
            float labelX = cx - getTextWidth(labelPaint, d.label) / 2;
//            float labelY = lineY + getPaintFontHeight(labelPaint) / 3 - dip2px(context, 5);
            float labelY = lineY + getPaintFontHeight(labelPaint) / 2 - dip2px(context, 6);
            canvas.drawText(d.label, labelX, labelY, labelPaint);

            //绘制标题
            if (i == count - 1) {
                float x = cx - getTextWidth(labelPaint, "标题") / 2;
                float y = lineY - getPaintFontHeight(titlePaint) - dip2px(context, 10);
                canvas.drawText("标题", x, y, titlePaint);
            }
        }
    }

    /**
     * 设置数据源
     *
     * @param chartData 数据源
     */
    public void setChartData(@NonNull FunnelData chartData) {
        this.funnelData = chartData;
        count = funnelData.funnels.size();
        mTopMaxLineHalf = lastLineOffset + getDefaultHalfWidthOffset() * count;
        invalidate();
    }

    /**
     * 默认漏斗宽度变化策略
     * */
    private float getDefaultHalfWidthOffset() {
//        if (count <= 4) {
//            return dip2px(context, 17);
//        } else if (count <= 6) {
            return dip2px(context, 5);
//        } else if (count <= 8) {
//            return dip2px(context, 10);
//        } else if (count <= 10) {
//            return dip2px(context, 7);
//        } else {
//            return dip2px(context, 5);
//        }
    }

    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,context.getResources().getDisplayMetrics());
    }

    /** dip转换px */
    public static float dip2px(Context context,int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,context.getResources().getDisplayMetrics());
    }

    /**
     * 禁止硬件加速
     * */
    public static void disableHardwareAccelerated(View view) {
        if (view == null) {
            return;
        }

        //是否开启了硬件加速,如开启将其禁掉
        if (!view.isHardwareAccelerated()) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /**
     * 得到单个字的高度
     * @param paint 画笔
     * @return 高度
     */
    public static float getPaintFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 得到一段文本的宽度
     * @param paint 画笔
     * @param str 文本
     * @return 文本宽度
     */
    public static float getTextWidth(Paint paint, String str) {
        if(str.length() == 0) return 0.0f;
        return paint.measureText(str, 0, str.length());
    }

    /**
     * 减法运算
     *
     * @param v1 参数1
     * @param v2 参数2
     * @return 运算结果
     */
    public static float sub(float v1, float v2) {
        BigDecimal bgNum1 = new BigDecimal(Float.toString(v1));
        BigDecimal bgNum2 = new BigDecimal(Float.toString(v2));
        return bgNum1.subtract(bgNum2).floatValue();
    }
}

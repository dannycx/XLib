package com.danny.xui.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;


import com.x.xtools.XArrayUtil;
import com.x.xtools.XUiUtil;
import com.x.xui.widget.chart.bean.XLevelChartBean;
import com.x.xui.widget.chart.bean.XLevelChartData;
import com.x.xui.widget.chart.bean.XLevelChartEntity;
import com.x.xui.widget.chart.bean.XLevelChartInfo;

import java.util.ArrayList;
import java.util.List;

public class LevelChartView extends View {
    private List<String> xLabel;
    private long startData;
    private long segmentLength;
    private List<Integer> dataList;
    private int xLabelLength;
    private int yLabelLength;
    private int xPoint;
    private int yPoint;
    private int xScale;
    private int originStartX;// 柱子其实x坐标值
    private int maxLength;// 最大宽度
    private int downCount = 0;// 点击次数

    private List<RectF> rectFList;// 记录点击位置
    private int clickPosition;// 点击位置
    private List<Float> avgs;// 平均值
    private List<Float> yLine = new ArrayList<>();// y轴竖线

    private Paint xPaint;// x轴画笔
    private Paint allPaint;// 全局画笔
    private Paint linePaint;// 画线
    private DashPathEffect pathEffect;// 虚线
    private DashPathEffect effect;// 实线
    private XLevelChartEntity entity;
    private XLevelChartInfo info;
    private LevelChartListener levelChartListener;

    private int yLabelTextWidth;// y轴文字最大宽度
    private int textMarginRight;// 文字右边距
    private int margin;
    private int dp3;
    private int dp0_33;
    private int dp4;
    private int dp8;
    private int dp10;
    private int dp11;
    private int dp15;
    private int sp9;


    public LevelChartView(Context context) {
        super(context);
        initParams(context);
    }

    private void initParams(Context context) {
        dp0_33 = XUiUtil.dp2px(context, 0.33f);
        dp3 = XUiUtil.dp2px(context, 3);
        dp4 = XUiUtil.dp2px(context, 4);
        dp8 = XUiUtil.dp2px(context, 8);
        dp10 = XUiUtil.dp2px(context, 10);
        dp11 = XUiUtil.dp2px(context, 11);
        dp15 = XUiUtil.dp2px(context, 15);
        sp9 = XUiUtil.sp2Px(context, 9);
        margin = 20;
        textMarginRight = dp10;
        yLabelTextWidth = XUiUtil.dp2px(context, 100);

        xPaint = new Paint();
        xPaint.setStyle(Paint.Style.FILL);
        xPaint.setAntiAlias(true);
        xPaint.setTextSize(sp9);
        xPaint.setColor(Color.parseColor("#666666"));

        allPaint = new Paint();
        allPaint.setStyle(Paint.Style.STROKE);
        allPaint.setDither(true);
        allPaint.setAntiAlias(true);
        allPaint.setColor(Color.BLACK);
        allPaint.setTextSize(sp9);

        pathEffect = new DashPathEffect(new float[] {15, 7}, 1);
        effect = new DashPathEffect(new float[] {0, 0}, 1);
        linePaint = new Paint();
        linePaint.reset();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setPathEffect(pathEffect);
        linePaint.setStrokeWidth(1);
        linePaint.setColor(Color.parseColor("#666666"));

        rectFList = new ArrayList<>();
    }

    public void setLevelChartListener(LevelChartListener levelChartListener) {
        this.levelChartListener = levelChartListener;
    }

    public void setData(XLevelChartEntity chartEntity, XLevelChartInfo chartInfo) {
        entity = chartEntity;
        info = chartInfo;
        dataList = getRateData();
        List<Integer> temp = dataList;
        LevelChartUtils.setMaxAndMinData(temp, entity);
        XLevelChartData chartData = LevelChartUtils.getXLabelData(entity.getMaxData(), entity.getMinData(), info.getxCount());
        startData = chartData.getStartData();
        segmentLength = chartData.getSegment();
        xLabel = LevelChartUtils.getXLabel(chartData.getxLabel());
        xLabelLength = xLabel.size();
        clickPosition = -1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (xLabel != null) {
            init();
            initCoordinate(canvas);
        }
    }

    private void init() {
        maxLength = getYLabelMaxWidth();
        if (maxLength > yLabelTextWidth) {
            maxLength = yLabelTextWidth;
        }

        originStartX = margin / 4 + maxLength + textMarginRight;
        xPoint = originStartX;

        int maxTxtWidth = LevelChartUtils.getTextWidth2(allPaint, entity.getMaxData() + entity.getUnit());// 柱子右侧最大值
        int maxWidth = getWidth() - originStartX - maxTxtWidth - dp10;
        maxWidth = maxWidth * 100;
        if (xLabelLength > 1) {
            for (int i = 0; i < xLabelLength; i++) {
                if (maxWidth % (xLabelLength - 1) == 0) {
                    xScale = maxWidth / (xLabelLength - 1);
                    break;
                }
                maxWidth = maxWidth + 1;
            }
            xScale = xScale / 100;
        } else {
            xScale = 0;
        }
    }

    private void initCoordinate(Canvas canvas) {
        int size = xLabel.size();
        yLine.clear();
        for (int i = 0; i < size; i++) {
            xPaint.setTextAlign(Paint.Align.LEFT);
            String labelStr = xLabel.get(i);
            int startX;
            startX = originStartX + i * xScale;

            if (i == size - 1) {
                startX -= (LevelChartUtils.getTextWidth2(xPaint, labelStr) / 2 + LevelChartUtils.getTextWidth2(xPaint, info.getUnit()));
                labelStr += info.getUnit();
            }

            canvas.drawText(labelStr, startX, getHeight() - margin / 6 - dp15, xPaint);

            if (i == size - 1) {
                startX += LevelChartUtils.getTextWidth2(xPaint, labelStr);
            } else if (i != 0) {
                startX += LevelChartUtils.getTextWidth2(xPaint, labelStr) / 2;
            }
            yLine.add((float) startX);
        }

        yPoint = (int) (getHeight() - dp15 - LevelChartUtils.getTextHeight(xPaint) - dp8);

        int length = 0;
        int typeSize = entity.getItems().size();
        for (int i = 0; i < typeSize; i++) {
            length += entity.getItems().get(i).getyLabels().size();
        }

        int stopY = (int) (yPoint - (length - 1) * dp11 - length * dp10
                - (typeSize - 1) * (LevelChartUtils.getTextHeight(allPaint) + dp11));
        for (int i = 0; i < yLine.size(); i++) {
            drawYLine(canvas, "#666666", yLine.get(i), yPoint, stopY, pathEffect, 1);
        }

        if (clickPosition != -1 && clickPosition < rectFList.size()) {
            RectF rectF = rectFList.get(clickPosition);
            rectF.top = rectF.top - dp4;
            rectF.bottom = rectF.bottom + dp4;
            rectF.right = yLine.get(yLine.size() -1);
            allPaint.setColor(Color.parseColor("#1A3986FF"));
            canvas.drawRoundRect(rectF, 0, 0, allPaint);
        }

        drawView(canvas);
    }

    private void drawYLine(Canvas canvas, String color, Float startX, int startY, int stopY, DashPathEffect effect, int width) {
        linePaint.setColor(Color.parseColor(color));
        linePaint.setPathEffect(effect);
        linePaint.setStrokeWidth(width);
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(startX, stopY);
        canvas.drawPath(path, linePaint);
    }

    // 画柱子
    private void drawView(Canvas canvas) {
        rectFList.clear();
        int startY = yPoint;
        int popStartY = yPoint;
        int popStopY;
        int temp = yPoint;

        allPaint.setStyle(Paint.Style.FILL);
        int group = entity.getItems().size();
        for (int j = group - 1; j >= 0; j--) {
            XLevelChartBean levelChartBean = entity.getItems().get(j);
            List<List<String>> colorNames = levelChartBean.getColorNames();
            List<String> yLabel = levelChartBean.getyLabels();
            yLabelLength = yLabel.size();

            for (int i = yLabelLength - 1; i >= 0; i--) {
                dataList = getRateIntData(levelChartBean.getxRates().get(i));

                // 画柱子
                int levelSize = levelChartBean.getxRates().size();
                float stopY = startY - dp10;
                if (levelSize == 1) {
                    allPaint.setColor(Color.parseColor(colorNames.get(i).get(0)));
                    float stopX = toX(dataList.get(0) - startData) + LevelChartUtils.getTextWidth2(xPaint, String.valueOf(dataList.get(0) / 100)) / 2;
                    if (dataList.get(0) == 0) {
                        stopX = originStartX;
                    }
                    RectF rectF = new RectF(originStartX, stopY, stopX, startY);
                    canvas.drawRoundRect(rectF, 0, 0, allPaint);
                    rectFList.add(yLabelLength - 1 - i, rectF);

                    // 右侧文字
                    allPaint.setColor(Color.parseColor("#666666"));
                    canvas.drawText(LevelChartUtils.getPercent(dataList.get(0), entity.getUnit()), stopX + dp8, startY - 5, allPaint);
                } else {
                    float maxStopX = 0;
                    for (int k = 0; k < levelSize; k++) {
                        allPaint.setColor(Color.parseColor(colorNames.get(i).get(k)));
                        stopY = startY - dp4;
                        float stopX = toX(dataList.get(k) - startData) + LevelChartUtils.getTextWidth2(xPaint, String.valueOf(dataList.get(k) / 100)) / 2;
                        if (dataList.get(k) == 0) {
                            stopY = originStartX;
                        }

                        if (stopX > maxStopX) {
                            maxStopX = stopX;
                        }
                        RectF rectF = new RectF(originStartX, stopY, stopX, startY);
                        canvas.drawRoundRect(rectF, 0, 0, allPaint);

                        if (k != levelSize - 1) {
                            startY -= dp0_33;
                        }
                    }
                    RectF rectF = new RectF(originStartX, stopY, maxStopX, stopY + levelSize * dp4 + (levelSize - 1) * dp0_33);
                    rectFList.add(xLabelLength - 1 - i, rectF);
                }

                // 画y轴
                String label = yLabel.get(i);
                allPaint.setColor(Color.parseColor("#999999"));
                int offsetX = LevelChartUtils.getTextWidth2(allPaint, label);
                float rectHeight = rectFList.get(xLabelLength - 1 - i).bottom - rectFList.get(xLabelLength - 1 - i).top;

                if (offsetX > yLabelTextWidth) {
                    TextPaint paint = new TextPaint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.parseColor("#999999"));
                    paint.setTextSize(sp9);
                    StaticLayout layout = new StaticLayout(label, paint, maxLength, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                    canvas.save();
                    canvas.translate(originStartX - textMarginRight - maxLength, temp - rectHeight / 4);
                    layout.draw(canvas);
                    canvas.restore();
                } else {
//                    canvas.drawText(label, originStartX - textMarginRight - maxLength, temp - rectHeight / 4, allPaint);
                    canvas.drawText(label, originStartX - textMarginRight - offsetX, temp - rectHeight / 4, allPaint);
                }

                // 向上移动
                startY = (int) (stopY - dp11);
            }

            popStopY = popStartY - yLabelLength * dp10 - (yLabelLength - 1) * dp11;

            String title = levelChartBean.getTitle();
            int titleOffset = LevelChartUtils.getTextWidth2(allPaint, title);
            allPaint.setColor(Color.parseColor("#111111"));
            allPaint.setFakeBoldText(true);
            canvas.drawText(title, originStartX - textMarginRight - titleOffset, startY, allPaint);
            allPaint.setFakeBoldText(false);

            if (info.isNeedVerticalLine()) {
                float avgStartX;
                if (!XArrayUtil.isEmpty(entity.getVerticals())) {// 竖线
                    List<String> verticals = entity.getVerticals();
                    String vertical = verticals.get(j);
                    if (TextUtils.isEmpty(vertical) || "-".equals(vertical)) {
                        verticals.set(j, "0");
                    }
                    float verticalFloat = Float.parseFloat(verticals.get(j));
                    avgStartX = toX((long) (verticalFloat * 100 - startData));
                    if (j != 0) {
                        avgStartX += LevelChartUtils.getTextWidth2(allPaint, String.valueOf(verticalFloat)) / 2;
                    }
                } else if (!TextUtils.isEmpty(entity.getTimeLine())) {// 时间线
                    float verticalFloat = Float.parseFloat(entity.getTimeLine());
                    avgStartX = toX((long) (verticalFloat * 100 - startData));
                    if (verticalFloat * 100 > Integer.parseInt(xLabel.get(j - 2))) {
                        avgStartX += LevelChartUtils.getTextWidth2(allPaint, xLabel.get(j - 1)) / 2;
                    } else {
                        avgStartX += LevelChartUtils.getTextWidth2(allPaint, entity.getTimeLine()) / 2;
                    }
                } else {// 平均值线
                    float avg = avgs.get(j);
                    avgStartX = toX((long) (avg - startData));
                    if (j != 0) {
                        avgStartX += LevelChartUtils.getTextWidth2(allPaint, String.valueOf(avg / 100)) / 2;
                    }
                }
                drawYLine(canvas, "#E7FFff", avgStartX, popStartY, popStopY, effect, 5);

                // 气泡
                String popStr = avgs.get(j) + entity.getUnit();
                if (!TextUtils.isEmpty(entity.getTimeLine())) {
                    popStr = entity.getTimeLine() + entity.getUnit();
                } else if (!XArrayUtil.isEmpty(entity.getVerticals())) {
                    popStr = entity.getVerticals().get(j) + entity.getUnit();
                }

                // 画气泡
                int popStrWidth = LevelChartUtils.getTextWidth2(allPaint, popStr) / 2;
                allPaint.setColor(Color.parseColor("#28fff226"));
                float startX = avgStartX - popStrWidth - dp10;
                float stopX = avgStartX + popStrWidth + dp10;
                float strStartX = avgStartX - popStrWidth;
                if (startX < originStartX) {
                    strStartX = strStartX + originStartX - startX;
                    stopX = originStartX + stopX - startX;
                    startX = originStartX;
                }

                RectF rectF = new RectF(startX, popStopY - 3 * dp4 - LevelChartUtils.getTextHeight(allPaint)
                        , stopX, popStopY - dp4);
                canvas.drawRoundRect(rectF, 10, 10, allPaint);

                // 画气泡边框
                allPaint.setColor(Color.parseColor("#2655354"));
                allPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRoundRect(rectF, 10, 10, allPaint);
                allPaint.setStyle(Paint.Style.FILL);

                // 画气泡文字
                allPaint.setColor(Color.parseColor("#fff226"));
                canvas.drawText(popStr, strStartX, popStopY - dp10, allPaint);
            }

            startY = (int) (startY - LevelChartUtils.getTextHeight(allPaint) - dp11);
            popStartY = startY;

        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (info != null && info.isChartClick()) {
            return super.onTouchEvent(event);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downCount = 0;
                if (clickPosition >= 0) {
                    dismissPop();
                } else {
                    invalidatePop(x, y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (downCount == 5) {
                    invalidatePop(x, y);
                } else {
                    downCount += 1;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 计算柱子终点x
     */
    private float toX(long num) {
        float x = 0;
        try {
            if (num == 0) {
                x = xPoint;
            } else {
                float a = num / segmentLength;
                x = xPoint + a * xScale;
            }
        } catch (Exception e) {
            return x;
        }

        return x;
    }

    public void dismissPop() {
        clickPosition = -1;
        if (levelChartListener != null) {
            levelChartListener.curveClick(-1, 0, 0);
        }
        invalidate();
    }

    private void invalidatePop(int x, int y) {
        int size = rectFList.size();
        for (int i = 0; i < size; i++) {
            RectF rectF = rectFList.get(i);
            if (rectF.contains(x, y)) {
                clickPosition = i;
                if (levelChartListener != null) {
                    levelChartListener.curveClick(clickPosition, x, (int) rectF.bottom);
                }
                invalidate();
            }
        }
    }

    private List<Integer> getRateData() {
        List<String> xLabels = new ArrayList<>();
        int size = entity.getItems().size();
        avgs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<List<String>> xRates = entity.getItems().get(i).getxRates();
            int xRateSize = xRates.size();
            int sum = 0;
            for (int j = 0; j < xRateSize; j++) {
                xLabels.addAll(xRates.get(j));
                int rateKSize = xRates.get(j).size();
                for (int k = 0; k < rateKSize; k++) {
                    String s = xRates.get(j).get(k);
                    if (TextUtils.isEmpty(s)) {
                        s = "0";
                    }
                    float tempFloat = Float.parseFloat(s) * 100;
                    sum += tempFloat;
                }
            }
            float avg = (float) sum / (float) xRateSize;
            avgs.add(avg);
        }
        return getRateIntData(xLabels);
    }

    private List<Integer> getRateIntData(List<String> xLabels) {
        List<Integer> intList = new ArrayList<>();
        int size = xLabels.size();
        for (int i = 0; i < size; i++) {
            String x = xLabels.get(i);
            if (TextUtils.isEmpty(x)) {
                x = "0";
            }
            float xLabel = (int) (Float.parseFloat(x) * 100);
            intList.add((int) xLabel);
        }
        return intList;
    }

    public int getClickPosition() {
        return clickPosition;
    }

    private int getYLabelMaxWidth() {
        int width = 0;
        int size = entity.getItems().size();
        List<String> yLabels = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            yLabels.addAll(entity.getItems().get(i).getyLabels());
        }

        int yLabelSize = yLabels.size();
        for (int i = 0; i < yLabelSize; i++) {
            int tempWidth = LevelChartUtils.getTextWidth2(allPaint, yLabels.get(i));
            if (width < tempWidth) {
                width = tempWidth;
            }
        }
        return width;
    }
}

package com.danny.xui.chart;

import android.graphics.Paint;
import android.text.TextUtils;

import com.x.xui.widget.chart.bean.XLevelChartData;
import com.x.xui.widget.chart.bean.XLevelChartEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelChartUtils {


    public static List<String> getXLabel(List<String> data) {
        List<String> result = new ArrayList<>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            String str = data.get(i);
            str = str.replace("K", "000");
            str = str.replace("W", "0000");
            float s = Float.parseFloat(str) / 100;
            DecimalFormat format = new DecimalFormat("###.###");
            result.add(format.format(s));
        }
        return result;
    }

    public static String getPercent(int data, String unit) {
        float x = data / 100;
        DecimalFormat format = new DecimalFormat("###.#");
        return format.format(x) + unit;
    }
    /**
     * 获取文本高度
     *
     * @param paint 画笔
     * @return 文本高度
     */
    public static float getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;// 文本下部距离 - 文本上部距离
    }

    /**
     * 获取文本宽度
     *
     * @param paint 画笔
     * @param text 文本
     * @return 文本宽度
     */
    public static float getTextWidth1(Paint paint, String text) {
        return paint.measureText(text);
    }

    /**获取文本宽度*/
    public static int getTextWidth2(Paint paint, String text) {
        int length = 0;
        if (!TextUtils.isEmpty(text)) {
            int len = text.length();
            float[] widths = new float[len];
            paint.getTextWidths(text, widths);
            for (int i = 0; i < len; i++) {
                length += (int) Math.ceil(widths[i]);
            }
        }
        return length;
    }

    /**
     * 获取x轴坐标
     *
     * @param maxData 最大值
     * @param minData 最小值
     * @param scaleSize x坐标个数 - 1
     *
     * @return 柱状图数据
     */
    public static XLevelChartData getXLabelData(int maxData, int minData, int scaleSize) {
        XLevelChartData data = new XLevelChartData();
        int max = maxData;
        int min = minData;
        int scale = 0;
        int noConvertScale = (max - min) / scaleSize;
        if (((max - min) % scaleSize == 0) && noConvertScale != 0
                && (noConvertScale % 1000 == 0 || noConvertScale % 100 ==0)) {
            scale = noConvertScale;
        } else if (noConvertScale > 1000) {
            if (min != 0 && min % 100 == 0) {
                scale = noConvertScale / 100 + 1;
                scale = scale * 100;
            } else {
                scale = noConvertScale / 1000 + 1;
                scale = scale * 1000;
            }
        } else if (noConvertScale > 100 || noConvertScale < 1000) {
            scale = noConvertScale / 100 + 1;
            scale = scale * 100;
        } else if (noConvertScale < 100) {
            if((max - min) % scaleSize == 0) {
                scale = noConvertScale;
            } else {
                scale = noConvertScale + 1;
            }
        } else {
            scale = noConvertScale;
        }


        data.setStartData(min);
        int changePercent = scale / 100;
        if (scale % 100 == 0) {
            scale = changePercent + 1;
            scale = scale * 100;
        }
        data.setSegment(scale);
        return getYLabel(data, scale, scaleSize);
    }

    private static XLevelChartData getYLabel(XLevelChartData data, int scale, int scaleSize) {
        List<String> xLabel = new ArrayList<>();
        List<Long> label = new ArrayList<>();
        for (int i = 0; i < scaleSize + 1; i++) {
            if (i == 0) {
                label.add((long) data.getStartData());
            } else {
                label.add(scale + label.get(i - 1));
            }
        }

        for (int i = 0; i < label.size(); i++) {
            if (label.get(2) == 0 && label.get(3) == 0 && label.get(i) == 0) {
                if (i == 0) {
                    xLabel.add("0");
                } else {
                    xLabel.add(i * 10 + "");
                }
            } else if (label.get(2) % 10000 == 0 && label.get(3) % 10000 == 0 && label.get(i) % 10000 == 0) {
                long j = label.get(i) / 10000;
                xLabel.add(j + "W");
            } else if (label.get(2) % 1000 == 0 && label.get(3) % 1000 == 0 && label.get(i) % 1000 == 0) {
                long j = label.get(i) / 1000;
                xLabel.add(j + "K");
            } else {
                long j = label.get(i);
                xLabel.add(j + "");
            }
        }
        data.setxLabel(xLabel);
        return data;
    }


    public static void setMaxAndMinData(List<Integer> data, XLevelChartEntity entity) {
        Collections.sort(data);
        entity.setMinData(data.get(0));
        entity.setMaxData((int) getMaxData(data.get(data.size() - 1), 0, false));
    }

    /**
     * 进位获取最大值
     * 50进60, 245进300...
     *
     * @param max 最大值
     * @param type 类型
     * @param isLimitMax
     * @return
     */
    private static float getMaxData(int max, int type, boolean isLimitMax) {
        if (max == 0) {
            if (0 == type) {
                max = 5;
            }
            return max;
        }
        int m = 1;
        float absMax = Math.abs(max);
        float abs = 1;
        if (max < 0) {
            abs = -1;
        }
        if (isLimitMax && absMax < 5) {
            abs *= 5;
        }
        int log = (int) Math.log10(absMax);
        log = log > 2 ? log - 1 : log;
        for (int i = 0; i < log; i++) {
            m *= 10;
        }

        float a = absMax / m;
        if (a == 9) {
            return abs * m * 10;
        }
        return abs * (a + 1) * m;
    }
}

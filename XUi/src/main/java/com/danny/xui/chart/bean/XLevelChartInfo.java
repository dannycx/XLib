package com.danny.xui.chart.bean;

public class XLevelChartInfo {
    private int xCount = 5;// x轴间隙个数
    private String unit;// 单位
    private String popRes;// 气泡背景
    private String popFrameRes;// 气泡边框颜色
    private String popTxtRes;// 气泡文字颜色
    private String ChartHeightRes;// 柱状图高度
    private boolean isShowFilter;// 是否显示筛选框
    private boolean isChartClick;// 是否可点
    private boolean isNeedVerticalLine;// 是否需要竖线

    public int getxCount() {
        return xCount;
    }

    public String getUnit() {
        return unit;
    }

    public String getPopRes() {
        return popRes;
    }

    public String getPopFrameRes() {
        return popFrameRes;
    }

    public String getPopTxtRes() {
        return popTxtRes;
    }

    public String getChartHeightRes() {
        return ChartHeightRes;
    }

    public boolean isShowFilter() {
        return isShowFilter;
    }

    public boolean isChartClick() {
        return isChartClick;
    }

    public boolean isNeedVerticalLine() {
        return isNeedVerticalLine;
    }
}

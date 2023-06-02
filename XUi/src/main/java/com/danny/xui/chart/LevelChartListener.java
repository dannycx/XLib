package com.danny.xui.chart;

public interface LevelChartListener {
    /**
     * 柱子点击事件
     *
     * @param pos 位置pos
     * @param x x坐标
     * @param y y坐标
     */
    void curveClick(int pos, int x, int y);
}

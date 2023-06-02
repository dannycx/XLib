package com.danny.xui.chart;

public interface LevelChartClickCallback {
    /**
     *
     * @param position 柱子位置
     * @param x x轴
     * @param y y轴
     */
    void click(int position, int x, int y);
}

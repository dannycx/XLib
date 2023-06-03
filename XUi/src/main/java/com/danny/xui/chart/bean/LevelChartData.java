package com.danny.xui.chart.bean;

import java.util.List;

public class LevelChartData {
    private List<String> xLabel;
    private long startData;
    private long segment;

    public List<String> getxLabel() {
        return xLabel;
    }

    public void setxLabel(List<String> xLabel) {
        this.xLabel = xLabel;
    }

    public long getStartData() {
        return startData;
    }

    public void setStartData(long startData) {
        this.startData = startData;
    }

    public long getSegment() {
        return segment;
    }

    public void setSegment(long segment) {
        this.segment = segment;
    }
}

package com.danny.xui.chart.bean;

import java.util.List;

public class LevelChartBean {
    private String title;
    private List<String> yLabels;
    private List<List<String>> xRates;
    private List<List<String>> colorNames;

    public String getTitle() {
        return title;
    }

    public List<String> getyLabels() {
        return yLabels;
    }

    public List<List<String>> getxRates() {
        return xRates;
    }

    public List<List<String>> getColorNames() {
        return colorNames;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setyLabels(List<String> yLabels) {
        this.yLabels = yLabels;
    }

    public void setxRates(List<List<String>> xRates) {
        this.xRates = xRates;
    }

    public void setColorNames(List<List<String>> colorNames) {
        this.colorNames = colorNames;
    }
}

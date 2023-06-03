package com.danny.xui.chart.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelChartEntity {
    private int maxData;
    private int minData;
    private String unit;// 柱子单位
    private String timeLine;// 单类别竖线
    private List<String> verticals;// 多类别竖线
    private List<LevelChartBean> items;// 柱状图数据
    private List<LevelChartPopBean> popBeanList;// pop
    private List<String> filters;// 筛选
    private String curFilter;// 当前选择项

    public void setItems(List<LevelChartBean> items) {
        this.items = items;
    }

    public LevelChartEntity getData() {
        LevelChartEntity entity = new LevelChartEntity();
        List<LevelChartBean> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            LevelChartBean bean = new LevelChartBean();
            bean.setTitle("text");
            List<String> yLabels = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                yLabels.add("ssss");
            }
            bean.setyLabels(yLabels);

            List<List<String>> xRates = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                List<String> li = new ArrayList<>();
                for (int k = 0; k < 1; k++) {
                    li.add(String.valueOf(new Random().nextInt(8) + i));
                }
                xRates.add(li);
            }
            bean.setxRates(xRates);
        }
        entity.setItems(list);
        return entity;
    }

    public void setMaxData(int maxData) {
        this.maxData = maxData;
    }

    public void setMinData(int minData) {
        this.minData = minData;
    }

    public int getMaxData() {
        return maxData;
    }

    public int getMinData() {
        return minData;
    }

    public String getUnit() {
        return unit;
    }

    public String getTimeLine() {
        return timeLine;
    }

    public List<String> getVerticals() {
        return verticals;
    }

    public List<LevelChartBean> getItems() {
        return items;
    }

    public List<LevelChartPopBean> getPopBeanList() {
        return popBeanList;
    }

    public List<String> getFilters() {
        return filters;
    }

    public String getCurFilter() {
        return curFilter;
    }
}

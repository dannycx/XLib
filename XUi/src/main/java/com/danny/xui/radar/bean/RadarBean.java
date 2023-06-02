package com.danny.xui.radar.bean;

import java.util.List;

public class RadarBean {
    List<String> brands;
    List<String> colors;
    List<String> xAxis;
    List<List<String>> yAxis;
    private List<Boolean> circleDraw;// 圆点
    private List<Boolean> dashLine;// 虚线
    private List<Boolean> fillDraw;// 填充
    private boolean reverseOrder;// 顺逆时针

    public List<String> getBrands() {
        return brands;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<String> getxAxis() {
        return xAxis;
    }

    public List<List<String>> getyAxis() {
        return yAxis;
    }

    public List<Boolean> getCircleDraw() {
        return circleDraw;
    }

    public List<Boolean> getDashLine() {
        return dashLine;
    }

    public List<Boolean> getFillDraw() {
        return fillDraw;
    }

    public boolean isReverseOrder() {
        return reverseOrder;
    }
}

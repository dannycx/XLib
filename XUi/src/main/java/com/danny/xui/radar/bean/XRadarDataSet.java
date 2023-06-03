package com.danny.xui.radar.bean;


import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;

import java.util.List;

public class XRadarDataSet extends RadarDataSet {
    private boolean circleDraw;// 圆点
    private boolean dashLine;// 虚线
    private boolean fillDraw;// 填充

    public XRadarDataSet(List<RadarEntry> yVals, String label) {
        super(yVals, label);
    }

    public boolean isCircleDraw() {
        return circleDraw;
    }

    public void setCircleDraw(boolean circleDraw) {
        this.circleDraw = circleDraw;
    }

    public boolean isDashLine() {
        return dashLine;
    }

    public void setDashLine(boolean dashLine) {
        this.dashLine = dashLine;
    }

    public boolean isFillDraw() {
        return fillDraw;
    }

    public void setFillDraw(boolean fillDraw) {
        this.fillDraw = fillDraw;
    }
}

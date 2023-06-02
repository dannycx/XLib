package com.danny.xui.radar;

import android.graphics.Canvas;
import android.graphics.Color;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRendererRadarChart;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.x.xtools.XArrayUtil;
import com.x.xtools.XSystemUtil;
import com.x.xtools.XThemeUtil;
import com.x.xtools.XUiUtil;

public class CustomXAxisRendererRadarChart extends XAxisRendererRadarChart {
    private RadarChart radarChart;
    private boolean isReverseOrder = false;// 正逆序

    public CustomXAxisRendererRadarChart(ViewPortHandler viewPortHandler, XAxis xAxis, RadarChart chart) {
        super(viewPortHandler, xAxis, chart);
        this.radarChart = chart;
    }

    public void setReverseOrder(boolean reverseOrder) {
        isReverseOrder = reverseOrder;
    }

    @Override
    public void renderAxisLabels(Canvas c) {
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            this.mAxisLabelPaint.setTypeface(this.mAxis.getTypeface());
            this.mAxisLabelPaint.setColor(this.mAxis.getLabelCount());
            this.mAxisLabelPaint.setTextSize(this.mAxis.getTextSize());

            float sliceAngle = radarChart.getSliceAngle();
            float scale = XSystemUtil.getInstance().getContext().getResources().getDisplayMetrics().density;
            float factor = scale * RadarChartUtil.BASE_FACTOR;

            MPPointF center = radarChart.getCenterOffsets();
            MPPointF out = MPPointF.getInstance(0f, 0f);

            int entryCount = this.radarChart.getData().getMaxEntryCountSet().getEntryCount();

            for (int i = 0; i < entryCount; i++) {
                String label = this.mXAxis.getValueFormatter().getFormattedValue(i, this.mAxis);
                if (isReverseOrder && i != 0) {
                    label = this.mXAxis.getValueFormatter().getFormattedValue(entryCount - i, this.mAxis);
                }
                int angle = (int) ((sliceAngle * (float) i + this.radarChart.getRotationAngle()) % 360.0f);
                mXAxis.mLabelRotatedWidth = XUiUtil.dp2px(XSystemUtil.getInstance().getContext(), RadarChartUtil.BASE_LABEL_WIDTH_DP);
                Utils.getPosition(center, factor * radarChart.getYRange() + mXAxis.mLabelRotatedWidth / 2.0f, angle, out);
                this.drawLabel(c, label, out.x, out.y, angle, i, entryCount);
            }

            MPPointF.recycleInstance(center);
            MPPointF.recycleInstance(out);
        }
    }

    private void drawLabel(Canvas c, String formattedLabel, float x, float y, float angleDegrees, int position, int count) {
        this.mAxisLabelPaint.setColor(Color.parseColor("#666666"));
        RadarChartUtil.drawXAxisValue(c, formattedLabel, x, y, this.mAxisLabelPaint, angleDegrees, position, count);
    }
}

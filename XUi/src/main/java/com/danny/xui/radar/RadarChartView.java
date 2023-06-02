package com.danny.xui.radar;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.x.xtools.XArrayUtil;
import com.x.xui.widget.radar.bean.RadarBean;
import com.x.xui.widget.radar.bean.XRadarDataSet;

import java.util.ArrayList;
import java.util.List;

public class RadarChartView extends RadarChart {
    private Context context;
    private RadarChartConfig chartConfig;

    public RadarChartView(Context context) {
        this(context, null);
    }

    public RadarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void init() {
        super.init();
        // 实现画虚线，圆点
        this.mRenderer = new RadarChartDrawLine(this, this.mAnimator, this.mViewPortHandler);
        this.mXAxisRenderer = new CustomXAxisRendererRadarChart(this.mViewPortHandler, this.mXAxis, this);
    }

    public void initRadarChartConfig(RadarChartConfig config) {
        if (config == null) {
            return;
        }
        this.chartConfig = config;
        setLogEnabled(true);
        setDrawMarkers(false);
//        setDrawingCacheEnabled(false);
        setTouchEnabled(false);// 屏蔽旋转

        getDescription().setEnabled(false);// 取消右下角描述
        float webLineWidth = config.getWebLineWidth();
        setWebLineWidth(webLineWidth);
        setWebLineWidthInner(webLineWidth);
        int webColorId = config.getWebColorId();
        setWebColor(webColorId);
        setWebColorInner(webColorId);
        getLegend().setEnabled(false);
        setNoDataText("");
        YAxis yAxis = getYAxis();
        yAxis.setDrawLabels(false);
        yAxis.setLabelCount(5);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        XAxis xAxis = getXAxis();
        xAxis.setAxisLineColor(Color.parseColor("#666666"));
        xAxis.setTextSize(10);
    }

    public void setData(RadarBean bean) {
        if (bean == null) {
            return;
        }
        ((RadarChartDrawLine) mRenderer).setReverseOrder(bean.isReverseOrder());
        ((CustomXAxisRendererRadarChart) mXAxisRenderer).setReverseOrder(bean.isReverseOrder());
        List<String> xAxis = bean.getxAxis();
        getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String s = "";
                try {
                    s = xAxis.get((int) value);
                } catch (IndexOutOfBoundsException e) {

                }
                return s;
            }
        });

        List<IRadarDataSet> sets = new ArrayList<>();

        if (bean.getBrands() != null && bean.getBrands().size() > 0
                && bean.getyAxis() != null && bean.getyAxis().size() > 0) {
            List<String> brands = bean.getBrands();
            int brandSize = brands.size();
            List<String> colors = bean.getColors();
            int colorSize = colors.size();
            int groupSize = bean.getyAxis().size();
            List<Float> yDatas;
            for (int i = 0; i < brandSize; i++) {
                if (i < groupSize) {
                    String brand = brands.get(i);
                    List<String> dataStr = bean.getyAxis().get(i);
                    if (dataStr != null && !dataStr.isEmpty()) {
                        yDatas = new ArrayList<>();
                        for (String str : dataStr) {
                            if (TextUtils.isEmpty(str)) {
                                yDatas.add(0f);
                            } else {
                                yDatas.add(Float.parseFloat(str));
                            }
                        }
                        boolean isDrawDash = false;
                        if (!XArrayUtil.isEmpty(bean.getDashLine())) {
                            isDrawDash = bean.getDashLine().get(i);
                        }
                        boolean isDrawCircle = false;
                        if (!XArrayUtil.isEmpty(bean.getCircleDraw())) {
                            isDrawCircle = bean.getCircleDraw().get(i);
                        }
                        boolean isDrawFill = true;
                        if (!XArrayUtil.isEmpty(bean.getFillDraw())) {
                            isDrawFill = bean.getFillDraw().get(i);
                        }
                        XRadarDataSet set = generateDataSet(brand, yDatas, isDrawDash, isDrawCircle, isDrawFill);
                        if (i < colorSize) {
//                            int color = XUiUtil.getColor(context, XUiUtil.getColorId(context, colors.get(i)));
                            int color = Color.parseColor(colors.get(i));
                            set.setColors(color);
                            set.setFillColor(color);
                        }

                        sets.add(set);
                    }

                }
            }
        }
        RadarData data = new RadarData(sets);
        setData(data);
        invalidate();
    }

    /**
     *
     * @param brand
     * @param yDatas
     * @param isDrawDash 虚线
     * @param isDrawCircle 圆点
     * @param isDrawFill 填充
     * @return
     */
    private XRadarDataSet generateDataSet(String brand, List<Float> yDatas, boolean isDrawDash, boolean isDrawCircle, boolean isDrawFill) {
        List<RadarEntry> entries = new ArrayList<>();
        if (yDatas != null) {
            int size = yDatas.size();
            for (int i = 0; i < size; i++) {
                Float f = yDatas.get(i);
                if (f == null) {
                    continue;
                }
                RadarEntry entry = new RadarEntry(100 * f);
                entries.add(entry);
            }
        }

        XRadarDataSet set = new XRadarDataSet(entries, brand);
        set.setDrawFilled(true);
        set.setDrawValues(false);
        set.setLineWidth(chartConfig == null ? 2 : chartConfig.getLineWidth());

        set.setCircleDraw(isDrawCircle);// 是否画圆点
        set.setDashLine(isDrawDash);// 是否画虚线
        set.setFillDraw(isDrawFill);// 是否填充
        return set;
    }

    @Override
    public void calculateOffsets() {
        float legendLeft = 0f;
        float legendTop = 0f;
        float legendRight = 0f;
        float legendBottom = 0f;
        float legendFullWidth;
        float legendOffset;
        float legendWidth;
        float legendHeight;
        float spacing;

        legendFullWidth = Utils.convertDpToPixel(this.mMinOffset);
        XAxis xAxis = getXAxis();
        xAxis.mLabelRotatedWidth = (int) Utils.convertDpToPixel(RadarChartUtil.BASE_LABEL_WIDTH_DP);
        if (mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled()) {
            legendFullWidth = Math.max(legendFullWidth, xAxis.mLabelRotatedWidth);
        }

        legendLeft += this.getExtraLeftOffset();
        legendRight += this.getExtraRightOffset();
        legendTop += this.getExtraTopOffset();
        legendBottom += this.getExtraBottomOffset();

        legendOffset = Math.max(legendFullWidth, legendLeft);
        spacing = Math.max(legendFullWidth, legendTop);
        legendWidth = Math.max(legendFullWidth, legendRight);
        legendHeight = Math.max(legendFullWidth, Math.max(this.getRequiredBaseOffset(), legendBottom));
        this.mViewPortHandler.restrainViewPort(legendOffset, spacing, legendWidth, legendHeight);
    }
}

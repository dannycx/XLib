package com.danny.xui.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.danny.xtool.UiTool;
import com.danny.xui.chart.bean.LevelChartInfo;

public class LevelChartPop extends RelativeLayout {
    private LevelChartInfo chartInfo;
    private LevelChartView chartView;
    private LinearLayout bubbleLayout;

    public LevelChartPop(Context context) {
        super(context);
    }

    public LevelChartPop(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LevelChartPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(Context context, LevelChartInfo info) {
        chartInfo = info;
        if (info == null) {
            chartInfo = new LevelChartInfo();
        }

        chartView = new LevelChartView(context);
        LayoutParams chartParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                UiTool.INSTANCE.dimensionPixelSize(context, chartInfo.getChartHeightRes()));
        chartParams.topMargin = 10;
//        chartParams.addRule(RelativeLayout.BELOW, getId());
        chartView.setLayoutParams(chartParams);
        addView(chartView);

        bubbleLayout = new LinearLayout(context);
        bubbleLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams bubbleParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bubbleLayout.setLayoutParams(bubbleParams);
        addView(bubbleLayout);
    }
}

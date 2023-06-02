package com.danny.xui.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.x.xtools.XThemeUtil;
import com.x.xtools.XUiUtil;
import com.x.xui.widget.chart.bean.XLevelChartInfo;


public class LevelChartPop extends RelativeLayout {
    private XLevelChartInfo chartInfo;
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

    public void initView(Context context, XLevelChartInfo info) {
        chartInfo = info;
        if (info == null) {
            chartInfo = new XLevelChartInfo();
        }

        chartView = new LevelChartView(context);
        LayoutParams chartParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                XUiUtil.getDimensionPixelSize(context, chartInfo.getChartHeightRes()));
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

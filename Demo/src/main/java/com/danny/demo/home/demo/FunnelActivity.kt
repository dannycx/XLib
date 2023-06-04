package com.danny.demo.home.demo

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.danny.demo.R
import com.danny.demo.databinding.ActivityFunnelBinding
import com.danny.xbase.base.BaseActivity
import com.danny.xui.radar.RadarChartConfig
import com.danny.xui.radar.RadarChartView
import com.danny.xui.radar.bean.RadarBean
import com.danny.xui.trend.FunnelData
import com.google.gson.Gson

/**
 *
 *
 * @author danny
 * @since 2020/12/16
 */
class FunnelActivity: BaseActivity() {
    private lateinit var funnelBinding: ActivityFunnelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        funnelBinding = DataBindingUtil.setContentView(this, R.layout.activity_funnel)

//        funnelBinding.funnelLayout.removeAllViews()
//        for (i in 0 .. 2) {
//            val lp = LinearLayout.LayoutParams(0
//                    , LinearLayout.LayoutParams.MATCH_PARENT)
//            lp.weight = 1f
//            val funnelView = FunnelView(this)
//            funnelView.layoutParams = lp
//            funnelBinding.funnelLayout.addView(funnelView)
//            funnelView.setChartData(FunnelData.getData())
//        }

//
//        funnelBinding.funnelView.setChartData(FunnelData.getData())

        funnelBinding.funnel.setChartData(FunnelData.getData())
        funnelBinding.funnel2.setChartData(FunnelData.getData())
        funnelBinding.funnel3.setChartData(FunnelData.getData())

//        funnelBinding.levelView.setData(XLevelChartEntity().data, XLevelChartInfo())


        initRadar()

//        val info = NavInfo()
//        info.title = "首页"
//        funnelBinding.nav.initView(info)
    }

    private fun initRadar() {
        val chartConfig = RadarChartConfig()
        chartConfig.lineWidth = 1f
        chartConfig.webLineWidth = 1f
        chartConfig.webColorId = Color.BLACK
        val chartView: RadarChartView = funnelBinding.radarView
        chartView.initRadarChartConfig(chartConfig)
        val json = """{
  "brands": ["小米", "三星", "华为", "平均值", "基线"],
  "colors": ["#ff0000", "#00ff00", "#0000ff", "#0f00f0", "#0f00ff"],
  "xAxis": ["超强的用户体验", "认知度高的", "值得拥有的", "高回收率的", "品牌信赖", "天资卓越的", "信心十足的"],
  "yAxis": [
    ["0.02", "0.25", "0.6", "0.56", "0.78", "0.45", "0.9"],
    ["0.32", "0.35", "0.16", "0.76", "0.98", "0.25", "0.59"],
    ["0.72", "0.65", "0.36", "0.26", "0.18", "0.95", "0.09"],
    ["0.72", "0.72", "0.72", "0.72", "0.72", "0.72", "0.72"],
    ["0.99", "0.99", "0.99", "0.99", "0.99", "0.99", "0.99"]
  ],
  "dashLine": [false, false, false, true, false],
  "circleDraw": [true, true, true, false, false],
  "fillDraw": [true, true, true, false, false]
}"""
        val bean: RadarBean = Gson().fromJson(json, RadarBean::class.java)
        chartView.setData(bean)
    }
}

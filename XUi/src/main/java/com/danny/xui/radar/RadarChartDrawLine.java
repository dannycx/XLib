package com.danny.xui.radar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import com.danny.xui.radar.bean.XRadarDataSet;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.renderer.RadarChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class RadarChartDrawLine extends RadarChartRenderer {
    private boolean isReverseOrder = false;// 正逆序

    public RadarChartDrawLine(RadarChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        this.mChart = chart;
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setColor(Color.rgb(255, 115, 115));
        this.mHighlightPaint.setStrokeWidth(2);
        this.mHighlightPaint.setStyle(Paint.Style.STROKE);

        this.mWebPaint = new Paint(1);
        this.mWebPaint.setStyle(Paint.Style.STROKE);

        this.mHighlightCirclePaint = new Paint(1);
    }

    public void setReverseOrder(boolean reverseOrder) {
        isReverseOrder = reverseOrder;
    }

    @Override
    protected void drawDataSet(Canvas c, IRadarDataSet dataSet, int mostEntries) {
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();
        float sliceAngle = mChart.getSliceAngle();

        // calculate the factor that is needed for transforming the value to pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0,0);
        Path surface = mDrawDataSetSurfacePathBuffer;
        surface.reset();

        boolean hasMovedToPoint = false;
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{8, 5}, 1);
        boolean allNull = true;
        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            if(dataSet.getEntryForIndex(i).getY() > 0){
                allNull = false;
                break;
            }
        }

        if (!allNull) {
            for (int j = 0; j < dataSet.getEntryCount(); j++) {
                mDrawPaint.setColor(dataSet.getColor(j));
                mRenderPaint.setColor(dataSet.getColor(j));
                RadarEntry e = dataSet.getEntryForIndex(j);

                if (isReverseOrder && j != 0) {
                    Utils.getPosition(
                            center,
                            (e.getY() - mChart.getYChartMin()) * factor * phaseY,
                            sliceAngle * (dataSet.getEntryCount() - j) * phaseX + mChart.getRotationAngle(), pOut);
                } else {
                    Utils.getPosition(
                            center,
                            (e.getY() - mChart.getYChartMin()) * factor * phaseY,
                            sliceAngle * j * phaseX + mChart.getRotationAngle(), pOut);
                }

                if (!Float.isNaN(pOut.x)) {
                    if (!hasMovedToPoint) {
                        surface.moveTo(pOut.x, pOut.y);
                        hasMovedToPoint = true;
                    } else {
                        surface.lineTo(pOut.x, pOut.y);
                    }
                    if (dataSet instanceof XRadarDataSet) {
                        if (((XRadarDataSet) dataSet).isCircleDraw()) {
                            c.drawCircle(pOut.x, pOut.y, 0.75f, mRenderPaint);
                        }

                        if (((XRadarDataSet) dataSet).isDashLine()) {
                            mDrawPaint.setPathEffect(dashPathEffect);
                        } else {
                            mDrawPaint.setPathEffect(null);
                        }

                        if (((XRadarDataSet) dataSet).isFillDraw()) {
                            mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
                        } else {
                            mRenderPaint.setStrokeWidth(5);
                        }
                    }
                }
            }
        }

        if (dataSet.getEntryCount() > mostEntries) {
            // if this is not the largest set, draw a line to the center before closing
            surface.lineTo(center.x, center.y);
        }

        surface.close();

        if (dataSet.isDrawFilledEnabled()) {
            final Drawable drawable = dataSet.getFillDrawable();
            if (drawable != null) {
                drawFilledPath(c, surface, drawable);
            } else {
                drawFilledPath(c, surface, dataSet.getFillColor(), dataSet.getFillAlpha());
            }
        }

        mRenderPaint.setStyle(Paint.Style.STROKE);

        // draw the line (only if filled is disabled or alpha is below 255)
        if (!dataSet.isDrawFilledEnabled() || dataSet.getFillAlpha() < 255)
            c.drawPath(surface, mRenderPaint);

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
    }
}

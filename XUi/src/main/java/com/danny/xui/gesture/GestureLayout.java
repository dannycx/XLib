package com.danny.xui.gesture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.danny.xtool.UiTool;
import com.danny.xui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码父容器
 * Created by 75955 on 2018/9/6.
 */
public class GestureLayout extends ViewGroup {
    private int base = 4;
    private int[] screenDisplay;
    private int blackWidth;
    private List<GesturePoint> pointList;
    private Context context;
    private boolean isVerify;
    private GestureDrawLine line;

    /**
     *
     * @param context
     * @param width
     * @param isVerify 是否验证密码
     * @param pwd 密码
     * @param callback 绘制完成回调
     */
    public GestureLayout(Context context, int width, boolean isVerify, String pwd, GestureDrawLine.Callback callback) {
        super(context);
        screenDisplay = UiTool.INSTANCE.screenDisplay(context);
        blackWidth = width == 0 ? screenDisplay[0] / 3 : width / 3;
        this.context = context;
        this.isVerify = isVerify;
        pointList = new ArrayList<>();
        addChild();
        line = new GestureDrawLine(context, isVerify, pwd, pointList, callback);
    }

    private void addChild() {
        for (int i = 0; i < 9; i++) {
            ImageView image = new ImageView(context);
            image.setBackgroundResource(R.mipmap.state_normal);
            this.addView(image);
            invalidate();
            int row = i / 3;
            int col = i % 3;
            int leftX = col * blackWidth + blackWidth / base;
            int rightX = col * blackWidth + blackWidth - blackWidth / base;
            int topY = row * blackWidth + blackWidth / base;
            int bottomY = row * blackWidth + blackWidth - blackWidth / base;
            GesturePoint point = new GesturePoint(leftX, rightX, topY, bottomY, image, i + 1);
            pointList.add(point);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int r, int t, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            int row = i / 3;
            int col = i % 3;
            View view = getChildAt(i);
            view.layout(col * blackWidth + blackWidth / base, row * blackWidth + blackWidth / base
                    , col * blackWidth + blackWidth - blackWidth / base, row * blackWidth + blackWidth - blackWidth / base);
        }
    }

    /**
     * 将手势布局添加到view中,父布局通常是FrameLayout
     *
     * @param parent 父布局
     */
    public void setParentView(ViewGroup parent) {
        int width = screenDisplay[0];
        LayoutParams params = new LayoutParams(width, width);
        this.setLayoutParams(params);
        line.setLayoutParams(params);
        parent.addView(line);
        parent.addView(this);
    }

    /**
     * 清除线
     *
     * @param delayTime 延时
     */
    public void clearDrawLineState(long delayTime) {
        line.clearDrawLineState(delayTime);
    }
}

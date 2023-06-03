package com.danny.xui.gesture;

import android.widget.ImageView;

import com.danny.xui.R;

/**
 * 手势密码点的bean
 * Created by 75955 on 2018/9/6.
 */
public class GesturePoint {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_SELECTED = 1;
    public static final int STATE_ERROR = 2;

    private int leftX;
    private int rightX;
    private int topY;
    private int bottomY;

    private int centerX;
    private int centerY;

    private ImageView view;

    private int pointState;// 圆点状态
    private int num;// 从1开始

    public GesturePoint(int leftX, int rightX, int topY, int bottomY, ImageView view, int num) {
        this.leftX = leftX;
        this.rightX = rightX;
        this.topY = topY;
        this.bottomY = bottomY;
        centerX = (leftX + rightX) / 2;
        centerY = (topY + bottomY) / 2;
        this.view = view;
        this.num = num;
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getTopY() {
        return topY;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
    }

    public int getPointState() {
        return pointState;
    }

    public void setPointState(int pointState) {
        this.pointState = pointState;
        switch (pointState) {
            case STATE_NORMAL:
                view.setBackgroundResource(R.mipmap.state_normal);
                break;
            case STATE_SELECTED:
                view.setBackgroundResource(R.mipmap.state_selected);
                break;
            case STATE_ERROR:
                view.setBackgroundResource(R.mipmap.state_error);
                break;
            default:
                break;
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GesturePoint that = (GesturePoint) o;

        if (leftX != that.leftX) return false;
        if (rightX != that.rightX) return false;
        if (topY != that.topY) return false;
        if (bottomY != that.bottomY) return false;
        if (view == null){
            if (that.view != null){
                return false;
            }
        }else if (!view.equals(that.view)){
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = prime * result + leftX;
        result = prime * result + (view != null ? view.hashCode() : 0);
        result = prime * result + rightX;
        result = prime * result + bottomY;
        result = prime * result + topY;
        return result;
    }
}

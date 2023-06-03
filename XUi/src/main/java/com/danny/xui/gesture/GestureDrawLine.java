package com.danny.xui.gesture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import com.danny.xtool.UiTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手势view
 * Created by 75955 on 2018/9/6.
 */

public class GestureDrawLine extends View {
    // 起始点
    private int movX;
    private int movY;
    private Paint paint;// 画笔
    private Canvas canvas;// 画布
    private Bitmap bitmap;// 位图
    private boolean isDrawEnable = true;// 是否可以绘制

    private int[] screenDisplay;
    private boolean isVerify;// 是否是校验密码
    private List<GesturePoint> pointList;
    private List<Pair<GesturePoint, GesturePoint>> pairs;// 记录选中线
    private Map<String, GesturePoint> autoSelected;// 自动选择点
    private Context context;
    private GesturePoint currentPoint;
    private Callback callback;
    private String password;
    private StringBuilder pwdSb;

    public GestureDrawLine(Context context, boolean isVerify, String pwd, List<GesturePoint> pointList, Callback callback) {
        super(context);
        screenDisplay = UiTool.INSTANCE.screenDisplay(context);
        this.context = context;
        this.callback = callback;
        paint = new Paint(Paint.DITHER_FLAG);
        canvas = new Canvas();
        bitmap = Bitmap.createBitmap(screenDisplay[0], screenDisplay[0], Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(0, 170, 238));
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        this.pointList = pointList;
        pairs = new ArrayList<>();
        initAutoGesturePoint();
        this.isVerify = isVerify;
        this.password = pwd;
        pwdSb = new StringBuilder();
    }

    private void initAutoGesturePoint() {
        autoSelected = new HashMap<>();
        autoSelected.put("1,3", getAutoGesturePoint(2));
        autoSelected.put("1,7", getAutoGesturePoint(4));
        autoSelected.put("1,9", getAutoGesturePoint(5));
        autoSelected.put("2,8", getAutoGesturePoint(5));
        autoSelected.put("3,7", getAutoGesturePoint(5));
        autoSelected.put("3,9", getAutoGesturePoint(6));
        autoSelected.put("4,6", getAutoGesturePoint(5));
        autoSelected.put("7,9", getAutoGesturePoint(8));
    }

    /**
     * 获取自动选中点
     *
     * @param num
     * @return
     */
    private GesturePoint getAutoGesturePoint(int num) {
        for (GesturePoint point : pointList) {
            if (point.getNum() == num) {
                return point;
            }
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDrawEnable == false) {
            return true;
        }
        paint.setColor(Color.rgb(0, 170, 238));
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                movX = (int) event.getX();
                movY = (int) event.getY();
                currentPoint = getPointAt(movX, movY);
                if (currentPoint != null){
                    currentPoint.setPointState(GesturePoint.STATE_SELECTED);
                    pwdSb.append(currentPoint.getNum());
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                ClearScreenDrawList();

                GesturePoint pointAt = getPointAt((int) event.getX(), (int) event.getY());
                if (currentPoint == null && pointAt == null){
                    return true;
                }else {
                    if (currentPoint == null){
                        currentPoint = pointAt;
                        currentPoint.setPointState(GesturePoint.STATE_SELECTED);
                        pwdSb.append(currentPoint.getNum());
                    }
                }

                // 移动到点为null或在当前点上或为选中状态，从当前点到手指位置画线
                if (pointAt == null || currentPoint.equals(pointAt) || pointAt.getPointState() == GesturePoint.STATE_SELECTED) {
                    canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), event.getX(), event.getY(), paint);
                }else {
                    canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), pointAt.getCenterX(), pointAt.getCenterY(), paint);
                    pointAt.setPointState(GesturePoint.STATE_SELECTED);

                    //获取是否有中间点
                    GesturePoint betweenPoint = getBetweenPoint(currentPoint, pointAt);

                    if (betweenPoint == null) {
                        Pair<GesturePoint, GesturePoint> pair = new Pair<>(currentPoint, pointAt);
                        pairs.add(pair);
                        currentPoint = pointAt;
                        pwdSb.append(currentPoint.getNum());
                    }else {
                        Pair<GesturePoint, GesturePoint> pair1 = new Pair<>(currentPoint, betweenPoint);
                        pairs.add(pair1);
                        betweenPoint.setPointState(GesturePoint.STATE_SELECTED);
                        pwdSb.append(betweenPoint.getNum());

                        Pair<GesturePoint, GesturePoint> pair2 = new Pair<>(betweenPoint, pointAt);
                        pairs.add(pair2);
                        currentPoint = pointAt;
                        pwdSb.append(currentPoint.getNum());
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!"".equals(pwdSb.toString())){
                    if (isVerify){
                        if (password.equals(pwdSb.toString())) {
                            callback.checkSuccess();
                        }else {
                            callback.checkError();
                        }
                    }else {
                        callback.gestureInput(pwdSb.toString());
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 选择点
     *
     * @param x 触摸x坐标
     * @param y 触摸y坐标
     * @return 触摸点
     */
    private GesturePoint getPointAt(int x, int y){
        for (GesturePoint point : pointList) {
            int leftX = point.getLeftX();
            int rightX = point.getRightX();
            if (!(leftX < x && x < rightX)) {
                continue;
            }

            int topY = point.getTopY();
            int bottomY = point.getBottomY();
            if (!(topY < y && y < bottomY)) {
                continue;
            }

            return point;
        }
        return null;
    }

    /**
     * 选择两点中间点
     *
     * @param start 开始点
     * @param end 结束点
     * @return 点
     */
    private GesturePoint getBetweenPoint(GesturePoint start, GesturePoint end) {
        int startNum = start.getNum();
        int endNum = end.getNum();
        String key = "";
        if (startNum > endNum) {
            key = endNum + "," + startNum;
        }else {
            key = startNum + "," + endNum;
        }
        return autoSelected.get(key);
    }

    /**
     * 延时清除绘制线状态
     * @param delayTime
     */
    public void clearDrawLineState(long delayTime) {
        if (delayTime > 0){
            isDrawEnable = false;
            clearDrawErrorLine();
        }
        new Handler().postDelayed(new ClearStateRunnable(), delayTime);
    }

    /**
     * 校验两次输入不一致，绘制错误线
     */
    private void clearDrawErrorLine() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setColor(Color.rgb(248, 69, 69));
        for (Pair<GesturePoint, GesturePoint> pair : pairs) {
            pair.first.setPointState(GesturePoint.STATE_ERROR);
            pair.second.setPointState(GesturePoint.STATE_ERROR);
            canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(), pair.second.getCenterX(), pair.second.getCenterY(), paint);
        }
        invalidate();
    }

    private class ClearStateRunnable implements Runnable {
        @Override
        public void run() {
            pwdSb = new StringBuilder();
            pairs.clear();
            ClearScreenDrawList();
            for (GesturePoint point : pointList) {
                point.setPointState(GesturePoint.STATE_NORMAL);
            }
            invalidate();
            isDrawEnable = true;
        }
    }

    /**
     * 清屏，绘制集合中线
     */
    private void ClearScreenDrawList() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (Pair<GesturePoint, GesturePoint> pair : pairs) {
            canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(), pair.second.getCenterX(), pair.second.getCenterY(), paint);
        }
    }

    public interface Callback {
        void gestureInput(String input);
        void checkSuccess();
        void checkError();
    }
}

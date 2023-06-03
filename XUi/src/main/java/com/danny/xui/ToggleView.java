package com.danny.xui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义开关
 * Created by danny on 2018/5/3.
 *
 * Android 界面流程
 * measure测量 -- layout摆放 -- draw绘制
 *
 * onResume后执行
 * view
 * onMeasure(指定自己宽高)        onDraw(绘制内容)
 * viewgroup
 * onMeasure(指定自己宽高,子控件宽高)    onLayout(摆放子控件)    onDraw(绘制内容)
 */

public class ToggleView extends View {
    private Bitmap bitmapSlideButton;
    private Bitmap bitmapSwitchBackground;
    private Paint paint;
    private boolean switchState=false;
    private boolean isTouchMode=false;
    private float currentX;
    private OnSwitchStateChangeListener mOnSwitchStateChangeListener;

    /**
     *代码创建控件
     * @param context
     */
    public ToggleView(Context context) {
        super(context);
        init();
    }

    /**
     *用于xml中使用，自定义属性
     * @param context
     * @param attrs
     */
    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        int background=attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/dannny.com.tools","background",-1);
        int button=attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/dannny.com.tools","slide_button",-1);
        switchState=attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res/dannny.com.tools","state",false);
        setBackgroundResource(background);
        setSlideButtonResource(button);
    }

    /**
     * 用于xml中使用，可指定样式，自定义属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {paint=new Paint();}

    /**
     * 设置控件大小-宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {setMeasuredDimension(bitmapSwitchBackground.getWidth(),bitmapSwitchBackground.getHeight());}

    /**
     * 画布
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawBitmap(bitmapSwitchBackground,0,0,paint);
        //绘制前景--滑块

        if (isTouchMode){
            //根据触摸位置画滑块
            //让滑块向左移动滑块宽度一半
            float newLeft= currentX - bitmapSlideButton.getWidth() / 2.0f;
            int maxLeft=bitmapSwitchBackground.getWidth()-bitmapSlideButton.getWidth();
            //限定滑块位置
            if (newLeft<0){
                newLeft=0;//左边范围
            }else if (newLeft>maxLeft){
                newLeft=maxLeft;//右边范围
            }
            canvas.drawBitmap(bitmapSlideButton,newLeft,0,paint);
        }else {
            //根据开关状态直接设置
            if (switchState){//开
                int newLeft=bitmapSwitchBackground.getWidth()-bitmapSlideButton.getWidth();
                canvas.drawBitmap(bitmapSlideButton,newLeft,0,paint);
            }else {
                canvas.drawBitmap(bitmapSlideButton,0,0,paint);
            }
        }
    }

    /**
     * 触摸事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouchMode=true;
                currentX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isTouchMode=false;
                currentX=event.getX();
                float half=bitmapSwitchBackground.getWidth()/2.0f;
                boolean state=currentX>half;//根据当前按下的位置和控件中心位置判断状态
                //开关状态变化通知界面状态更新
                if (state != switchState && mOnSwitchStateChangeListener != null){
                    mOnSwitchStateChangeListener.onStateChange(state);//最新状态
                }
                switchState=state;
                break;
            default:break;
        }
        invalidate();//重绘界面，onDraw()重新执行
        return true;//消费用户触摸事件，才可接收其他事件
    }

    /**
     * 设置背景图片
     * @param resId
     */
    public void setSwitchBackgroundResource(int resId){bitmapSwitchBackground= BitmapFactory.decodeResource(getResources(),resId);}

    /**
     * 设置滑块背景图片
     * @param resId
     */
    public void setSlideButtonResource(int resId){bitmapSlideButton=BitmapFactory.decodeResource(getResources(),resId);}

    /**
     * 设置开关状态
     * @param flag
     */
    public void setSwitchState(boolean flag){switchState=flag;}

    public interface OnSwitchStateChangeListener{void onStateChange(boolean state);}

    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener onSwitchStateChangeListener){mOnSwitchStateChangeListener=onSwitchStateChangeListener;}
}

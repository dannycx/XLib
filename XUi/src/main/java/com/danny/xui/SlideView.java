package com.danny.xui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 侧滑面板
 * Created by danny on 2018/5/6.
 */
public class SlideView extends ViewGroup {
    private static final int OPEN = 0;
    private static final int CLOSE = 1;
    private View mLeft;
    private View mMain;

    private Scroller mScroller;

    private float mDownX;
    private float mMoveX;
    private float mDownY;
    private float mMoveY;

    private int mCurrentState = CLOSE;

    public SlideView(Context context) {
        super(context);
        init();
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化滚动器，数值模拟器
        mScroller = new Scroller(getContext());
    }

    /**
     * 测量并设置子view宽高
     * @param widthMeasureSpec 当前控件宽度测量规则
     * @param heightMeasureSpec 当前控件高度测量规则
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //指定左面板宽高
        mLeft=getChildAt(0);
        mLeft.measure(mLeft.getLayoutParams().width,heightMeasureSpec);

        //指定主面板宽高
        mMain=getChildAt(1);
        mMain.measure(widthMeasureSpec,heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     *
     * @param changed 当前控件尺寸大小，位置是否改变
     * @param left 左边距
     * @param top 顶边距
     * @param right 右边距
     * @param bottom 下边距
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mLeft.layout(-mLeft.getLayoutParams().width,0,0,bottom);
        mMain.layout(left,top,right,bottom);
    }

    //scrollTo(int x,int y)滚动控件到某个位置-直接跳转
    //x:x轴坐标
    //y:y轴坐标
    //scrollBy(int x,int y)滚动控件到某个位置-原基础跳转
    //x:x轴坐标
    //y:y轴坐标

    /**
     * 触摸事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX=event.getX();
                int scrollX= (int) (mDownX-mMoveX);//将要发生偏移量
                int newScrollX= (int) (getScaleX()+scrollX);//计算将要滚动到位置，是否超出范围
                //限定左边距
                if (newScrollX < -mLeft.getMeasuredWidth()){
                    scrollTo(-mLeft.getMeasuredWidth(),0);
                }else if (newScrollX > 0){
                    scrollTo(0,0);
                }else {
                    scrollBy(scrollX, 0);
                }
                mDownX=mMoveX;
                break;
            case MotionEvent.ACTION_UP:
                //根据当前位置决定动画
                int leftCenter = (int) (-mLeft.getMeasuredWidth() / 2.0f);
                if (getScrollX() < leftCenter){//开启
                    mCurrentState=OPEN;
                    updateLayout();
                }else {
                    mCurrentState=CLOSE;
                    updateLayout();
                }
                break;
            default:break;
        }
        return true;
    }

    private void updateLayout() {
        int startX=getScrollX();
        int dx=0;
        //平滑动画
        if (mCurrentState == OPEN){
//            scrollTo(-mLeft.getMeasuredWidth(), 0);
            //结束位置-开始位置
            dx = -mLeft.getMeasuredWidth() - startX;
        }else {
//            scrollTo(0,0);
            dx = 0 - startX;
        }
        //开始平滑数据模拟
        //开始x,开始y,x偏移，y偏移，时长
        int duration = Math.abs(dx * 2);
        mScroller.startScroll(startX,0,dx,0,duration);

        invalidate();//重绘界面-drawChild()-draw()
    }

    /**
     * 维持动画继续
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            //动画还没结束
            int currentX=mScroller.getCurrX();//获取当前模拟数据==要滚动到位置
            scrollTo(currentX,0);
            invalidate();//重绘界面
        }
    }

    private void open(){
        mCurrentState=OPEN;
        updateLayout();
    }

    private void close(){
        mCurrentState=CLOSE;
        updateLayout();
    }

    public void switchState(){
        if (mCurrentState==OPEN){
            open();
        }else {
            close();
        }
    }

    public int getCurrentState(){return mCurrentState;}

    //拦截判断
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX=ev.getX();
                mDownY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX=Math.abs(ev.getX()-mDownX);
                float offsetY=Math.abs(ev.getY()-mDownY);
                if (offsetX>offsetY && offsetX > 10){//拦截-水平距离超出一定范围才拦截
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}

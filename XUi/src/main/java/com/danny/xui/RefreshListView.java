package com.danny.xui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.xui.R;

import java.text.SimpleDateFormat;


/**
 * 刷新 侧滑
 * Created by danny on 2018/5/6.
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final int PULL_REFRESH = 0;
    private static final int RELEASE_REFRESH = 1;
    private static final int REFRESHING = 2;

    private View mHeaderView;//头布局
    private int mHeaderViewHeight;//头布局高度
    private View mFooterView;//尾布局
    private int mFooterViewHeight;//尾布局高度
    private boolean mIsLoadMore=false;

    //Touch事件
    private float mDownY;
    private float mMoveY;
    private float mUpY;
    private int mPaddingTop;

    //刷新模式
    private int mCurrentRefreshMode = PULL_REFRESH;

    //view
    private TextView mRefreshTitle;
    private TextView mRefreshTime;
    private ProgressBar mRefreshProgress;
    private ImageView mRefreshArrow;

    private RotateAnimation mHeaderRotateUp;
    private RotateAnimation mHeaderRotateDown;

    public RefreshListView(Context context) {
        super(context);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initHeader();
        initAnimation();
        initFooter();
        setOnScrollListener(this);
    }

    private void initAnimation() {
        mHeaderRotateUp = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mHeaderRotateUp.setDuration(300);
        mHeaderRotateUp.setFillAfter(true);//动画停在结束位置

        mHeaderRotateDown = new RotateAnimation(-180f, -360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mHeaderRotateDown.setDuration(300);
        mHeaderRotateDown.setFillAfter(true);//动画停在结束位置
    }

    private void initHeader() {
        mHeaderView = View.inflate(getContext(), R.layout.list_view_header, null);
        mRefreshArrow = mHeaderView.findViewById(R.id.header_refresh_arrow);
        mRefreshProgress = mHeaderView.findViewById(R.id.header_refresh_progress);
        mRefreshTitle = mHeaderView.findViewById(R.id.header_refresh_title);
        mRefreshTime = mHeaderView.findViewById(R.id.header_refresh_time);
        mRefreshTime.setText("最后刷新时间:"+getLastRefreshTime());
        mHeaderView.measure(0, 0);//手动测量宽高，按照设置规则测量
//        int height = mHeaderView.getHeight();
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        //设置内边距，可隐藏控件
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        addHeaderView(mHeaderView);
    }

    private void initFooter() {
        mFooterView = View.inflate(getContext(), R.layout.list_view_footer, null);
        mFooterView.measure(0, 0);//手动测量宽高，按照设置规则测量
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        //设置内边距，可隐藏控件
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        addFooterView(mFooterView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = ev.getY();

                if (mCurrentRefreshMode == REFRESHING) {
                    return super.onTouchEvent(ev);
                }
                float offset = mMoveY - mDownY;
                //偏移量大于0，并且为第一个条目
                if (offset > 0 && getFirstVisiblePosition() == 0) {
                    mPaddingTop = (int) (-mHeaderViewHeight + offset);
                    mHeaderView.setPadding(0, mPaddingTop, 0, 0);
                    if (mPaddingTop >= 0 && mCurrentRefreshMode != RELEASE_REFRESH) {
                        //变为刷新模式
                        mCurrentRefreshMode = RELEASE_REFRESH;
                        upDateHeader();
                    } else if (mPaddingTop < 0 && mCurrentRefreshMode != PULL_REFRESH) {
                        //下拉刷新模式
                        mCurrentRefreshMode = PULL_REFRESH;
                        upDateHeader();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //根据移动距离
                if (mPaddingTop < 0) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                } else {
                    mHeaderView.setPadding(0, 0, 0, 0);
                    mCurrentRefreshMode = REFRESHING;
                    upDateHeader();
                }
                //根据状态
//                if (mCurrentRefreshMode== PULL_REFRESH){
//                     mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
//                 }else if (mCurrentRefreshMode==RELEASE_REFRESH){
//                    mHeaderView.setPadding(0,0,0,0);
//                    mCurrentRefreshMode=REFRESHING;
//                    upDateHeader();
//                }
//                mUpY=ev.getY();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void upDateHeader() {
        switch (mCurrentRefreshMode) {
            case PULL_REFRESH:
                mRefreshArrow.startAnimation(mHeaderRotateDown);
                mRefreshTitle.setText("下拉刷新");
                break;
            case RELEASE_REFRESH:
                mRefreshArrow.startAnimation(mHeaderRotateUp);
                mRefreshTitle.setText("释放刷新");
                break;
            case REFRESHING:
                mRefreshArrow.clearAnimation();
                mRefreshArrow.setVisibility(INVISIBLE);
                mRefreshProgress.setVisibility(VISIBLE);
                mRefreshTitle.setText("正在刷新");
                if (mRefreshListener!=null){
                    mRefreshListener.onRefresh();//回调刷新数据,刷新完毕调用onRefreshComplete
                }
                break;
        }
    }

    public void onRefreshComplete(){
        if (mIsLoadMore){
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            mIsLoadMore=false;
        }else {
            mCurrentRefreshMode = PULL_REFRESH;
            mRefreshTitle.setText("下拉刷新");
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mRefreshArrow.setVisibility(VISIBLE);
            mRefreshProgress.setVisibility(INVISIBLE);
            String time = getLastRefreshTime();
            mRefreshTime.setText("最后刷新时间:" + time);
        }
    }

    private String getLastRefreshTime() {
        long time=System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {//状态改变
        if (mIsLoadMore){return;}
        //scrollState--空闲、触摸、滑翔
        if (scrollState == SCROLL_STATE_IDLE && getLastVisiblePosition() >= (getCount()-1)){
            mIsLoadMore=true;
            Log.d("RefreshListView", "onScrollStateChanged: 开始加载更多");
            mFooterView.setPadding(0,0,0,0);
            setSelection(getCount());
            if (mRefreshListener!=null){
                mRefreshListener.onLoad();
            }
        }else if (scrollState == SCROLL_STATE_TOUCH_SCROLL){
            Log.d("RefreshListView", "onScrollStateChanged: 触摸");
        }else if (scrollState == SCROLL_STATE_FLING){
            Log.d("RefreshListView", "onScrollStateChanged: 滑翔");
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {//滑动
    }

    private OnRefreshListener mRefreshListener;

    public interface OnRefreshListener{
        void onRefresh();
        void onLoad();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener){mRefreshListener=onRefreshListener;}
}

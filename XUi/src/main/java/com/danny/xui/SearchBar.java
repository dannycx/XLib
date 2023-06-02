package com.danny.xui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.x.xtools.Constants;

import java.util.List;

/**
 * 快速索引view,定位指定字母
 * Created by 75955 on 2018/8/20.
 */
public class SearchBar extends View {
    private List<String> mLetter;
    private Paint mPaint = new Paint();
    private boolean hasBg;
    private int lastPos = -1;
    private Context mContext;
    private int allHeight;
    private TextView mTextView;
    private OnSearchChangedListener mListener;

    public SearchBar(Context context) {
        this(context,null);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setAllHeight();
        Log.d(Constants.TAG, "SearchBar: ");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(Constants.TAG, "dispatchTouchEvent: ");
        final int action = event.getActionMasked();
        float y = event.getY();
        int oldPos = lastPos;
        int i = (int) (y / allHeight * mLetter.size());
        OnSearchChangedListener listener = mListener;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                hasBg = true;
                if (i != oldPos && listener != null) {
                    if (i > 0 && i < mLetter.size()) {
                        listener.searchChanged(mLetter.get(i));
                        lastPos = i;
                        invalidate();
                        setText(i);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (i != oldPos && listener != null) {
                    if (i > 0 && i < mLetter.size()) {
                        listener.searchChanged(mLetter.get(i));
                        lastPos = i;
                        invalidate();
                        setText(i);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                hasBg = false;
                lastPos = -1;
                invalidate();
                if (mTextView != null) {
                    mTextView.setVisibility(GONE);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(Constants.TAG, "onMeasure: ");
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpec = MeasureSpec.makeMeasureSpec(allHeight, heightMode);
        setMeasuredDimension(widthMeasureSpec, heightSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(Constants.TAG, "onDraw: ");
        if (mLetter.size() == 0){
            return;
        }
        if (hasBg){
            canvas.drawColor(Color.TRANSPARENT);
        }
        int height = allHeight;
        int width = getWidth();
        int singleHeight = height / mLetter.size();
        for (int i = 0; i < mLetter.size(); i++) {
            mPaint.setColor(Color.parseColor("#999999"));
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(18);
            int x = (int) (width / 2 - mPaint.measureText(mLetter.get(i)) / 2);
            int y = singleHeight * i + singleHeight;
            canvas.drawText(mLetter.get(i), x, y, mPaint);
            mPaint.reset();
        }
    }

    /**
     * 设置TextView,用于提示当前选中字母
     *
     * @param textView 显示选中字母
     */
    public void setTextView(TextView textView) {
        mTextView = textView;
    }

    /**
     * 设置TextView显示字母
     *
     * @param position 选中字母在列表中位置
     */
    private void setText(int position) {
        Log.d(Constants.TAG, "setText: ");
        if (mTextView != null){
            mTextView.setVisibility(VISIBLE);
            mTextView.setText(mLetter.get(position));
        }
    }

    /**
     * 设置列表中字母集合
     *
     * @param letter 字母集合
     */
    public void setLetter(List<String> letter) {
        mLetter = letter;
        setAllHeight();
        requestLayout();
    }

    /**
     * 设置快速查找列表高度
     */
    private void setAllHeight() {
        if (mLetter != null) {
            allHeight = (int) (mLetter.size() * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics()));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public interface OnSearchChangedListener {
        void searchChanged(String letter);
    }

    public void setListener(OnSearchChangedListener listener) {
        mListener = listener;
    }
}

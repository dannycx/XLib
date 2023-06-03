package com.danny.xui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局,添加子view,作为筛选条件
 * @author danny
 * @since 2018-11-6.
 */
public class FluidLayout extends ViewGroup {
    @ViewDebug.ExportedProperty(category = "measurement", flagMapping = {
            @ViewDebug.FlagToString(mask = Gravity.TOP, equals = Gravity.TOP, name = "TOP"),
            @ViewDebug.FlagToString(mask = Gravity.BOTTOM, equals = Gravity.BOTTOM, name = "BOTTOM"),
            @ViewDebug.FlagToString(mask = Gravity.CENTER, equals = Gravity.CENTER, name = "CENTER")
    }, formatToHexString = true)
    private int mGravity = Gravity.TOP;

    private List<List<View>> views = new ArrayList<>();
    private List<Integer> lineHeights = new ArrayList<>();

    public FluidLayout(Context context) {
        this(context, null);
    }

    public FluidLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FluidLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FluidLayout);
        int index = array.getInt(R.styleable.FluidLayout_LayoutParams1, -1);
        if (index != -1) {
            setGravity(index);
        }
        array.recycle();
    }

    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int widthLine = 0;
        int heightLine = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (widthLine + childWidth > widthSize - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(widthLine, width);
                widthLine = childWidth;
                height += heightLine;
                heightLine = childHeight;
            }else {
                widthLine += childWidth;
                heightLine = Math.max(heightLine, childHeight);
            }
            if (i == count) {
                width = Math.max(widthLine, width);
                height += heightLine;
            }
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : width + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? heightSize : height + getPaddingTop() + getPaddingBottom()
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        views.clear();
        lineHeights.clear();

        int width = getWidth();
        int widthLine = 0;
        int heightLine = 0;
        List<View> lineViews= new ArrayList<>();

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (widthLine + childWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
                lineHeights.add(heightLine);
                views.add(lineViews);

                widthLine = 0;
                heightLine = childHeight + lp.topMargin + lp.bottomMargin;
                lineViews = new ArrayList<>();
            }

            widthLine += childWidth + lp.leftMargin + lp.rightMargin;
            heightLine = Math.max(heightLine, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }

        lineHeights.add(heightLine);
        views.add(lineViews);

        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineCount = views.size();
        for (int i = 0; i < lineCount; i++) {
            lineViews = views.get(i);
            heightLine = lineHeights.get(i);

            int lineViewCount = lineViews.size();

            for (int j = 0; j < lineViewCount; j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == GONE) {
                    continue;
                }

                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = mGravity;
                }

                int tmpTop = top;
                if (gravity == Gravity.CENTER) {
                    tmpTop = top + (heightLine - lp.topMargin - lp.bottomMargin - child.getMeasuredHeight()) / 2;
                }else if (gravity == Gravity.BOTTOM) {
                    tmpTop = top + (heightLine - lp.topMargin - lp.bottomMargin - child.getMeasuredHeight());
                }

                int lv = left + lp.leftMargin;
                int tv = tmpTop + lp.topMargin;
                int rv = lv + child.getMeasuredWidth();
                int bv = tv + child.getMeasuredHeight();

                child.layout(lv, tv, rv, bv);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            left = getPaddingLeft();
            top += heightLine;
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends MarginLayoutParams {
        @ViewDebug.ExportedProperty(category = "layout", mapping = {
                @ViewDebug.IntToString(from = Gravity.TOP, to = "TOP"),
                @ViewDebug.IntToString(from = Gravity.BOTTOM, to = "BOTTOM"),
                @ViewDebug.IntToString(from = Gravity.CENTER, to = "CENTER")
        })
        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.FluidLayout_LayoutParams);
            gravity = array.getInt(R.styleable.FluidLayout_LayoutParams_LayoutParams2, -1);
            array.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(FrameLayout.LayoutParams params) {
            super(params);
            this.gravity = params.gravity;
        }
    }
}

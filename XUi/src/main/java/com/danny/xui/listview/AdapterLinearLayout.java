package com.danny.xui.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by danny on 2019/1/17.
 *
 */
public class AdapterLinearLayout extends LinearLayout {
    private BaseListAdapter baseAdapter;

    public AdapterLinearLayout(Context context) {
        super(context);
        init();
    }

    public AdapterLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdapterLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public BaseListAdapter getBaseAdapter() {
        return baseAdapter;
    }

    public void setBaseAdapter(BaseListAdapter baseAdapter) {
        this.baseAdapter = baseAdapter;
        removeAllViews();
        for (int i = 0; i < baseAdapter.getItemCount(); i++) {
            BaseViewHolder viewHolder = baseAdapter.createViewHolder(this, baseAdapter.getItemViewType(i));
            addView(viewHolder.itemView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            baseAdapter.onCreateViewHolder(this, i);
        }
    }
}

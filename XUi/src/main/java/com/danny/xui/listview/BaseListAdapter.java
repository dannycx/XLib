package com.danny.xui.listview;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by danny on 2019/1/17.
 *
 */
public abstract class BaseListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

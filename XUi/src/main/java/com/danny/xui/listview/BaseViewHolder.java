package com.danny.xui.listview;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by danny on 2019/1/17.
 *
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    OnItemClickListener itemClickListener;
    OnItemLongClickListener itemLongClickListener;
    View itemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(this, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (itemLongClickListener != null) {
            itemLongClickListener.onItemLongClick(this, getAdapterPosition());
            return true;
        }
        return false;
    }

    public interface OnItemClickListener {
        void onItemClick(BaseViewHolder holder, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(BaseViewHolder holder, int position);
    }
}

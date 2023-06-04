package com.danny.xbase.base.nav;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danny.xbase.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danny on 2018/11/8.
 * 侧边栏适配器
 */
public class NavAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NavBean> beans;
    private OnItemClick itemClick;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case NavBean.CATEGORY:
                return new CategoryHolder(inflater.inflate(R.layout.nav_category_item, parent, false));
            case NavBean.ITEM:
                return new ItemHolder(inflater.inflate(R.layout.nav_item_item, parent,false));
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = beans.get(position).getType();
        switch (type) {
            case NavBean.CATEGORY:
                ((CategoryHolder)holder).title.setText(beans.get(position).getTitle());
                break;
            case NavBean.ITEM:
                ((ItemHolder)holder).image.setImageResource(beans.get(position).getDrawable());
                ((ItemHolder)holder).title.setText(beans.get(position).getTitle());
                ((ItemHolder)holder).root.setOnClickListener(view -> {
                    if (itemClick != null) {
                        itemClick.onItemClick(beans.get(position).getTag());
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return beans != null ? beans.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return beans.get(position).getType();
    }

    public ArrayList<NavBean> getBeans() {
        return beans;
    }

    public void setBeans(ArrayList<NavBean> beans) {
        this.beans = beans;
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder {
        TextView title;
        public CategoryHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.nav_category_title);
        }
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        View root;
        ImageView image;
        TextView title;

        public ItemHolder(View itemView) {
            super(itemView);
            root = itemView;
            image = itemView.findViewById(R.id.nav_item_image);
            title = itemView.findViewById(R.id.nav_item_title);
        }
    }

    public interface OnItemClick {
        void onItemClick(int tag);
    }
}

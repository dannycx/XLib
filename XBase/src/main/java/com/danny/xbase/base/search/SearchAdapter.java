package com.danny.xbase.base.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.danny.xbase.databinding.LayoutSearchItemBinding;

import java.util.List;

class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {
    private List<String> dataList;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutSearchItemBinding binding = LayoutSearchItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        Holder holder = new Holder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        String data = dataList.get(i);
        holder.setData(data);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class Holder extends RecyclerView.ViewHolder {
        LayoutSearchItemBinding binding;

        public Holder(@NonNull LayoutSearchItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(String data) {
            binding.setData(data);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String data);
    }
}

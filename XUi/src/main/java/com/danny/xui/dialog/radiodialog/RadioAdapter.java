package com.danny.xui.dialog.radiodialog;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danny.xui.R;

import java.util.ArrayList;

/**
 * Created by danny on 2018/12/7.
 * 条目适配器
 */
public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.Holder> {
    private ArrayList<RadioBean> list;
    private int selectPos = 0;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_radio,
            parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.setItem(position);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public ArrayList<RadioBean> getList() {
        return list;
    }

    public void setList(ArrayList<RadioBean> list) {
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                selectPos = i;
                break;
            }
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        private View item;
        private TextView title;
        private ImageView icon;

        public Holder(View itemView) {
            super(itemView);
            item = itemView;
            title = itemView.findViewById(R.id.pop_radio_item_title);
            icon = itemView.findViewById(R.id.pop_radio_item_icon);
        }

        public void setItem(final int position) {
            title.setText(list.get(position).getValue());
            icon.setImageResource(list.get(position).isChecked() ? R.mipmap.icon_gesture_tip_normal
                : R.mipmap.icon_gesture_tip_pressed);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    if (selectPos != pos) {
                        list.get(selectPos).setChecked(false);
                        list.get(pos).setChecked(false);
                        notifyItemChanged(pos);
                        notifyItemChanged(selectPos);
                        selectPos = pos;
                    }
                }
            });
        }
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }

    public int getSelectPos() {
        return selectPos;
    }
}

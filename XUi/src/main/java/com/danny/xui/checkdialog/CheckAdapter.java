package com.danny.xui.checkdialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.xui.R;

import java.util.ArrayList;
import java.util.List;

public class CheckAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;
    private List<String> selects;

    public CheckAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        selects = new ArrayList<>();
    }

    public CheckAdapter(Context context, List<String> datas, List<String> selects) {
        this.context = context;
        this.datas = datas;
        this.selects = selects;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.check_layout, null);
            holder.ivCheck = convertView.findViewById(R.id.iv_check);
            holder.tvName = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvName.setText(datas.get(position));
        if (isSelectContainsCurrent(datas.get(position))) {
//            holder.ivCheck.setBackground();
        } else {
//            holder.ivCheck.setBackground();
        }
        return convertView;
    }

    private boolean isSelectContainsCurrent(String current) {
        int size = selects.size();
        for (int i = 0; i < size; i++) {
            if (selects.get(i).equals(current)) {
                return true;
            }
        }
        return false;
    }

    class Holder {
        ImageView ivCheck;
        TextView tvName;
    }

    public void clearData(ListView listView) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();
        int len = datas.size();
        for (int i = 0; i < len; i++) {
            if (i >= firstVisiblePosition || i <= lastVisiblePosition) {
                for (int j = 0; j < selects.size(); j++) {
                    if(selects.get(j).equals(datas.get(i))) {
                        View v = listView.getChildAt(i - firstVisiblePosition);
                        Holder holder = (Holder) v.getTag();
//                        holder.ivCheck.setBackground();
                        selects.remove(datas.get(i));
                    }
                }
            }
        }
    }

    public void updateData(int position, ListView listView, int limit) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();
        if (position >= firstVisiblePosition || position <= lastVisiblePosition) {
            View v = listView.getChildAt(position - firstVisiblePosition);
            Holder holder = (Holder) v.getTag();
            boolean flag = isSelectContainsCurrent(datas.get(position));
            if (!flag) {
                if (limit != 0 && limit > selects.size()) {
                    selects.add(datas.get(position));
//                    holder.ivCheck.setBackground();
                } else {
                    Toast.makeText(context, "qqqqq", Toast.LENGTH_SHORT).show();
                }
            } else {
//                holder.ivCheck.setBackground();
                selects.remove(datas.get(position));
            }
        }
    }
}

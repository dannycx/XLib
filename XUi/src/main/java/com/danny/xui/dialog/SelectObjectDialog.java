package com.danny.xui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.danny.xtool.UiTool;
import com.danny.xui.R;
import com.danny.xui.listview.BaseViewHolder;

/**
 * Created by 75955 on 2019/1/24.
 * 条目选择对话框
 */
public class SelectObjectDialog extends Dialog {
    private Context context;
    private ListView listView;
    private TextView title;
    private String[] objects;

    public SelectObjectDialog(Context context, int styleId, String title, String[] objects) {
        super(context);
        this.context = context;
        init();
        initAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_object_select);
        initView();
        initData();
    }

    private void initView() {
        title = findViewById(R.id.dialog_title);
        listView = findViewById(R.id.dialog_list);
    }

    private void initData() {

    }

    public void init() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = UiTool.INSTANCE.displayWidth(context) - UiTool.INSTANCE.dp2px(context, 30);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void initAdapter() {
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return objects.length;
            }

            @Override
            public Object getItem(int i) {
                return objects[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                return null;
            }
        });
    }

    class Holder extends BaseViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }
}

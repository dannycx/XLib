package com.danny.xui.dialog.radiodialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danny.xtool.UiTool;
import com.danny.xui.R;

import java.util.ArrayList;

/**
 * 单选对话框
 * @author danny
 * 2018/11/5.
 */
public class RadioListPopupWindow extends PopupWindow {
    private Context context;
    private int itemHeight;
    private int maxCount;
    private View view;
    private ArrayList<RadioBean> list;
    private OnRadioCallback callback;
    private TextView title;
    private RecyclerView recyclerView;
    private RadioAdapter adapter;
    private Button ok;
    private int position = 0;

    public RadioListPopupWindow(ArrayList<RadioBean> list) {
        this.list = list;
    }

    public void init(Context context, int itemHeight, int maxCount) {
        this.context = context;
        this.itemHeight = itemHeight;
        this.maxCount = maxCount;
        initView();
        initPopupWindow();
    }

    private void initPopupWindow() {
        setFocusable(true);
        //外部点击取消显示
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());// 必须指定

        setContentView(view);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER) {
                    confirmClick();
                    return true;
                }
                return false;
            }
        });

        setWidth(context.getResources().getDimensionPixelSize(com.danny.common.R.dimen.dp_293));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 外部点击不消失
//        setOutsideTouchable(false);
//        setBackgroundDrawable(null);
    }

    private void setParams() {
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        if (list.size() > maxCount) {
            params.height = maxCount * itemHeight;
        }else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        recyclerView.setLayoutParams(params);
    }

    private void initView() {
        view = UiTool.INSTANCE.inflate(context, R.layout.dialog_radio_layout);
        title = view.findViewById(R.id.pop_radio_title);
        recyclerView = view.findViewById(R.id.pop_radio_recycler);
        ok = view.findViewById(R.id.pop_radio_ok);
        ok.setOnClickListener(view -> confirmClick());
        initRecycleView();
    }

    private void confirmClick() {
        if (callback != null) {
            callback.onRadioSelect(adapter.getSelectPos());
            dismiss();
        }
    }

    private void initRecycleView() {
        setParams();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RadioAdapter();
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        adapter.setSelectPos(0);
        UiTool.INSTANCE.activityAlpha((Activity) context, false);
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public void setList(ArrayList<RadioBean> list) {
        this.list = list;
        setParams();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    public void setCallback(OnRadioCallback callback) {
        this.callback = callback;
    }

    public void scrollTop() {
        recyclerView.scrollToPosition(0);
    }
}

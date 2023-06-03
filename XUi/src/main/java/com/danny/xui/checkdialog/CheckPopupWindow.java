package com.danny.xui.checkdialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.danny.xtool.UiTool;
import com.danny.xui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private CheckAdapter adapter;
    private List<String> dataList;
    private List<String> selectList;
    private CompleteListener listener;
    private int limitCount;// 限制选择个数
    private ListView listView;
    private Button complete;
    private Button reSet;
    private CheckBean checkBean;

    @SuppressLint("ResourceAsColor")
    public CheckPopupWindow(Context context, CheckBean checkBean) {
        super(context);
        this.context = context;
        this.checkBean = checkBean;
        View view = LayoutInflater.from(context).inflate(R.layout.check_pop_layout, null);
        setContentView(view);

        initView(view);
        initAdapter();
        initData();


        setBackgroundDrawable(new ColorDrawable(0));
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);

        setWidth(UiTool.INSTANCE.dp2px(context, 500));
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (listener != null) {
                    listener.reSet();
                }
            }
        });
    }

    private void initView(View view) {
        listView = view.findViewById(R.id.list_view);
        complete = view.findViewById(R.id.complete);
        reSet = view.findViewById(R.id.reset);
    }

    private void initAdapter() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.updateData(position, listView, limitCount);
            }
        });
        complete.setOnClickListener(this);
        reSet.setOnClickListener(this);
    }

    private void initData() {
        selectList = new ArrayList<>();
        dataList = checkBean.getName();

        if (dataList.size() > 5) {
            ViewGroup.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, UiTool.INSTANCE.dp2px(context, 177));
            listView.setLayoutParams(params);
        }
        adapter = new CheckAdapter(context, dataList);
        listView.setAdapter(adapter);
        listView.setVerticalScrollBarEnabled(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.complete) {
            if (selectList.size() < 2) {
                Toast.makeText(context, "不能少于2个", Toast.LENGTH_SHORT).show();
                return;
            }
            listener.complete(changeListToString(selectList));
        } else if (id == R.id.reset) {
            adapter.clearData(listView);
            selectList.clear();
        }
    }

    public void update(String str) {
        selectList = changeStringToList(str);
        adapter = new CheckAdapter(context, dataList, selectList);
        listView.setAdapter(adapter);
    }

    private List<String> changeStringToList(String select) {
        if (TextUtils.isEmpty(select)) {
            return new ArrayList<>();
        }
        String[] arr = select.split(",");
        selectList = Arrays.asList(arr);
        return selectList;
    }

    private String changeListToString(List<String> list) {
        String str = list.toString();
        String result = str.substring(1, str.length() - 1);
        return result.replaceAll(", ", ",");
    }

    public interface CompleteListener {
        void complete(String select);
        void reSet();
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    public void setListener(CompleteListener listener) {
        this.listener = listener;
    }
}

package com.danny.xui.itemclick;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.danny.xui.R;

public class ItemClickView extends FrameLayout {
    private TextView mTitle;
    private TextView mDes;

    public ItemClickView(@NonNull Context context) {
        this(context, null);
    }

    public ItemClickView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemClickView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml--->view	将设置界面的一个条目转换成view对象,直接添加到了当前SettingItemView对应的view中
        View.inflate(context, R.layout.item_click_view, this);

        //自定义组合控件中的控件
        mTitle = findViewById(R.id.tv_title);
        mDes = findViewById(R.id.tv_des);
    }


    public void setDes(String des) {
        mDes.setText(des);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }
}

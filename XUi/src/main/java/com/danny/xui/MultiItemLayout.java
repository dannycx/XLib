package com.danny.xui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;

import com.x.xtools.XArrayUtil;
import com.x.xui.R;

import java.util.HashSet;
import java.util.List;

/**
 * Created by danny on 2019/2/28.
 * 多选条目控件
 */
public class MultiItemLayout extends LinearLayout implements View.OnClickListener {
    private Context context;
    private ColorStateList csl;
    private LinearLayout layoutContent;
    private List<String> ids;

    public MultiItemLayout(Context context) {
        this(context, null);
    }

    public MultiItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View.inflate(context, R.layout.multi_item_layout, this);
        layoutContent = findViewById(R.id.multi_layout);

    }

    /** 设置view选中状态 */
    public void setItemSelect(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
        } else {
            v.setSelected(true);
        }
    }

    public void setSelectList(List<String> list) {
        HashSet<String> set = new HashSet<>();
        if (!XArrayUtil.isEmpty(list)) {

        }
    }

    public void setSelectList(HashSet<String> set) {

    }

    public void createText() {
        TextView tv = new TextView(context);
        tv.setTag(R.id.multi_tag_id, 1);
        tv.setTextColor(csl);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
    }

    @Override
    public void onClick(View view) {

    }
}

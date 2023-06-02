package com.danny.xui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.x.xtools.XUiUtil;
import com.x.xui.R;

/**
 * 提示信息view,新消息小红点显示消息个数
 * Created by 75955 on 2018/9/13.
 */

public class TipMsgLayout extends LinearLayout {
    private LinearLayout layout;
    private TextView tip;
    private Context context;

    public TipMsgLayout(Context context) {
        this(context, null);
    }

    public TipMsgLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipMsgLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_tip_msg, this, true);
        initView();
    }

    private void initView() {
        layout = findViewById(R.id.ll_tip_msg);
        tip = findViewById(R.id.tip_msg_count);
    }

    public void setTip(int count) {
        if (count <= 0){
            tip.setTextSize(0);
            tip.setText(null);
            LayoutParams params = new LayoutParams(0, 0);
            layout.setLayoutParams(params);
        }else if (count < 10) {
            tip.setTextSize(9);
            tip.setText(count + "");
            LayoutParams params = new LayoutParams(XUiUtil.dp2px(context, 12), XUiUtil.dp2px(context, 12));
            layout.setLayoutParams(params);
        }else if (count < 100) {
            tip.setTextSize(9);
            tip.setText(count + "");
            LayoutParams params = new LayoutParams(XUiUtil.dp2px(context, 18), XUiUtil.dp2px(context, 18));
            layout.setLayoutParams(params);
        }else {
            tip.setTextSize(9);
            tip.setText(99 + "+");
            LayoutParams params = new LayoutParams(XUiUtil.dp2px(context, 22), XUiUtil.dp2px(context, 22));
            layout.setLayoutParams(params);
        }
    }

    public void hide() {
        layout.setVisibility(GONE);
    }
}

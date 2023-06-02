package com.danny.xui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.xui.R;


/**
 * @author danny
 * @date 2018/9/27
 *
 * @description 成功失败提示对话框
 */

public class ResultDialog extends Dialog {
    private String result;
    private int drawable;

    public ResultDialog(Context context, String result, int drawable) {
        super(context);
        this.result = result;
        this.drawable = drawable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_result);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        ImageView icon = findViewById(R.id.dialog_result_icon);
        icon.setImageResource(drawable);

        TextView tip = findViewById(R.id.dialog_result_tip);
        tip.setText(result);
    }

}

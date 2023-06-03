package com.danny.xui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import com.danny.xui.R;

/**
 * 对话框工具
 * @author danny
 * 2018/11/4.
 */
public class SimpleDialogHelper {

    /** 普通对话框使用 */
    public static void commonDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.mipmap.icon_notify);
        builder.setTitle("");
        builder.setMessage("");
        builder.setPositiveButton("", null);
        builder.setNegativeButton("", null);
        builder.show();
    }

    /** 注意:兼容低版本 */
    public void note(Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        View view = new View(activity);
        dialog.setView(view, 0, 0, 0, 0);// 去除内边距(黑色边框)
    }
}

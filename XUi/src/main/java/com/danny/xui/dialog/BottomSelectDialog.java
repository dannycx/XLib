package com.danny.xui.dialog;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 底部对话框
 * @author danny
 * 2018/11/13.
 */
public class BottomSelectDialog {
    private static volatile BottomSelectDialog instance = null;
    private static Context context;
    private static volatile BottomSheetDialog bottomSheetDialog;
    private static View bottomSheetDialogView;

    private BottomSelectDialog() {}

    public static BottomSelectDialog newInstance(Context c, int layoutId) {
        context = c;
        if (instance == null) {
            synchronized (BottomSelectDialog.class) {
                if (instance == null) {
                    instance = new BottomSelectDialog();
                }
            }
        }
        if (bottomSheetDialogView == null) {
            synchronized (BottomSelectDialog.class) {
                bottomSheetDialogView = LayoutInflater.from(c).inflate(layoutId, null);
                bottomSheetDialog = new BottomSheetDialog(c);
                bottomSheetDialog.setContentView(bottomSheetDialogView);
            }
        }
        return instance;
    }

    public static void showBottomSheetDialog() {
        if (!((Activity)context).isFinishing() && bottomSheetDialog != null) {
            bottomSheetDialog.show();
        }
    }

    public static void dismissBottomSheetDialog() {
        if (bottomSheetDialogView != null) {
            bottomSheetDialogView = null;
        }
        bottomSheetDialog.dismiss();
    }

    public static BottomSheetDialog getBottomSheetDialog() {
        if (bottomSheetDialog != null) {
            return bottomSheetDialog;
        }
        return null;
    }

    public static View getBottomSheetDialogView() {
        if (bottomSheetDialogView != null) {
            return bottomSheetDialogView;
        }
        return null;
    }
}

package com.danny.xui.dialog.radiodialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.danny.xtool.UiTool;

import java.util.ArrayList;

/**
 * Created by danny on 2018/12/6.
 * 单选列表使用
 */
public class RadioHelper {
    private static RadioListPopupWindow popupWindow;
    public static void show(View v, final Context context, ArrayList<String> list, String title) {
        ArrayList<RadioBean> beans = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RadioBean bean = new RadioBean();
            bean.setValue(list.get(i));
            if (i == 0) {
                bean.setChecked(true);
            }
            beans.add(bean);
        }
        if (popupWindow == null) {
            popupWindow = new RadioListPopupWindow(beans);
            popupWindow.init(context, UiTool.INSTANCE.dimensionPixelSize(context,
                com.danny.common.R.dimen.dp_48), 6);
            popupWindow.setTitle(title);
            popupWindow.setClippingEnabled(true);
            popupWindow.setCallback(position ->
                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show());
        }else {
            popupWindow.setList(beans);
        }

        popupWindow.scrollTop();
        popupWindow.showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0,
            context.getResources().getDimensionPixelSize(com.danny.common.R.dimen.dp_11));
        UiTool.INSTANCE.activityAlpha((Activity) context, true);
    }
}

package com.danny.xui.dialog.radiodialog;

/**
 * Created by danny on 2018/12/6.
 * popupWindow数据源
 */
public class RadioBean {
    private String value;
    private boolean isChecked;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

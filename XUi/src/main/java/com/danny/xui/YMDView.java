package com.danny.xui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 年月日选择器
 *
 * @author danny
 * @since 2018-10-29.
 */
public class YMDView extends FrameLayout implements RadioGroup.OnCheckedChangeListener {
    private static final int YEAR = 0;
    private static final int MONTH = 1;
    private static final int DAY = 2;

    private Callback callback;

    private RadioButton year;
    private RadioButton month;
    private RadioButton day;

    public YMDView(Context context) {
        this(context, null);
    }

    public YMDView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YMDView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_radio_select, this, true);
        RadioGroup group = findViewById(R.id.group);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        day = findViewById(R.id.day);
        group.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (R.id.year == checkedId) {
            setBg(YEAR);
            if (callback != null) {
                callback.selected(YEAR);
            }
        }else if (R.id.month == checkedId) {
            setBg(MONTH);
            if (callback != null) {
                callback.selected(MONTH);
            }
        }else if (R.id.day == checkedId) {
            setBg(DAY);
            if (callback != null) {
                callback.selected(DAY);
            }
        }
    }

    public void setBg(int tag) {
        switch (tag) {
            case YEAR:
                year.setBackground(getContext().getDrawable(R.drawable.shape_ymd));
                month.setBackground(null);
                day.setBackground(null);
                break;
            case MONTH:
                year.setBackground(null);
                month.setBackground(getContext().getDrawable(R.drawable.shape_ymd));
                day.setBackground(null);
                break;
            case DAY:
                year.setBackground(null);
                month.setBackground(null);
                day.setBackground(getContext().getDrawable(R.drawable.shape_ymd));
                break;
            default:break;
        }
    }

    public interface Callback {
        void selected(int tag);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}

package com.danny.xui;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 跑马灯效果
 * @author danny
 * @since 2018-10-16.
 *
 * TextView添加如下属性也可实现
 *      android:focusable="true"
 *      android:focusableInTouchMode="true"
 *      android:marqueeRepeatLimit="marquee_forever"
 */
public class MarqueeView extends androidx.appcompat.widget.AppCompatTextView {

    public MarqueeView(Context context) {
        super(context);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}

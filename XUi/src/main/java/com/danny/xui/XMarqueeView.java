package com.danny.xui;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @description 跑马灯效果
 * @author danny
 * @date 2018/10/16.
 *
 * TextView添加如下属性也可实现
 *      android:focusable="true"
 *      android:focusableInTouchMode="true"
 *      android:marqueeRepeatLimit="marquee_forever"
 */

public class XMarqueeView extends androidx.appcompat.widget.AppCompatTextView {

    public XMarqueeView(Context context) {
        super(context);
    }

    public XMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XMarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}

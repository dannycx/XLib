package com.danny.xui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * 粘性控件,类似QQ新消息小红点
 * @author danny
 * @since 2018-11-18.
 */
public class ViscosityView extends View {
    public ViscosityView(Context context) {
        super(context);
        init();
    }

    public ViscosityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViscosityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

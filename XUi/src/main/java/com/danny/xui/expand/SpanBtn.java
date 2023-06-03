package com.danny.xui.expand;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by 75955 on 2018/10/6.
 */

public class SpanBtn extends ClickableSpan {
    private View.OnClickListener listener;
    private int color;
    private Context context;

    public SpanBtn(View.OnClickListener listener, Context context) {
        this(listener, com.danny.common.R.color.color_007eff, context);
    }

    public SpanBtn(View.OnClickListener listener, int color, Context context) {
        this.listener = listener;
        this.color = color;
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(context.getResources().getColor(color));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }
}

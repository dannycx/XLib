package com.danny.xui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.danny.xui.R;

public class FullScreenDialog extends Dialog {
    private int type;

    public FullScreenDialog(Context context, int type) {
        super(context, R.style.FullScreenDialog);
        this.type = type;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_full_screen, null);
        FrameLayout layout = view.findViewById(R.id.full_screen_contains);
        setContentView(layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        if (type == 1) {
            initDialog(layout);
        }
    }

    private void initDialog(FrameLayout layout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_activities, null);
        ImageView iv = view.findViewById(R.id.activities_close);
        TextView tv = view.findViewById(R.id.activities_btn);
        layout.addView(view);
        layout.setClickable(true);
        iv.setOnClickListener(v -> {
            FullScreenDialog.this.cancel();
            if (onActivitiesListener != null) {
                onActivitiesListener.close();
            }
        });

        tv.setOnClickListener(v -> {
            FullScreenDialog.this.cancel();
            if (onActivitiesListener != null) {
                onActivitiesListener.other();
            }
        });
    }

    private OnActivitiesListener onActivitiesListener;

    public void setOnActivitiesListener(OnActivitiesListener onActivitiesListener) {
        this.onActivitiesListener = onActivitiesListener;
    }

    public interface OnActivitiesListener {
        void close();
        void other();
    }
}

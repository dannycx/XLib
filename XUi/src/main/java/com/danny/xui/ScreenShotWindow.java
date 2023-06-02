package com.danny.xui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.x.xtools.XUiUtil;
import com.x.xui.R;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by 75955 on 2019/3/22.
 * 截图界面
 */

public class ScreenShotWindow extends AppCompatActivity implements View.OnClickListener {
    public static final String IMAGE_PATH = "image_path";
    private static final long DELAY_FINISH = 7000;
    private ImageView image;
    private Handler handler;
    private FrameLayout layout;
    private String path;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_screen_shot);
        Intent intent = getIntent();
        if (intent != null) {
            path = intent.getStringExtra(IMAGE_PATH);
        }
        initWindow();
        initView();
        handler = new Handler();
        if (!TextUtils.isEmpty(path)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Glide.with(ScreenShotWindow.this).load(new File(path))
                            .into(new WeakReference<>(image).get());
                }
            }, 500);
        }
    }

    private void initView() {
        image = findViewById(R.id.image);
        layout = findViewById(R.id.create_task);
        layout.setOnClickListener(this);
    }

    private void initWindow() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = XUiUtil.dp2px(this, 204);
        lp.width = XUiUtil.dp2px(this, 118);
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        lp.dimAmount = 0f;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 7000);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "截图创建任务", Toast.LENGTH_SHORT).show();
    }
}

package com.danny.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.danny.demo.R;

/**
 * Created by danny on 2018/5/9.
 */
public class BackgroundActivity extends Activity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        ImageView iv_bottom = findViewById(R.id.bottom);
        ImageView iv_top = findViewById(R.id.top);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        iv_bottom.startAnimation(alphaAnimation);
        iv_top.startAnimation(alphaAnimation);

        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}

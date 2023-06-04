package com.danny.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.danny.demo.service.HuojianService;
import com.danny.demo.R;

/**
 * 火箭开关
 * Created by danny on 2018/5/9.
 */

public class HuoJianActivity extends Activity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huojian);
        mContext=this;

        Button open= (Button) findViewById(R.id.open);
        open.setOnClickListener(view -> {
            startService( new Intent(mContext, HuojianService.class));
            finish();
        });

        Button close= (Button) findViewById(R.id.close);
        close.setOnClickListener(view -> stopService(new Intent(mContext,HuojianService.class)));
    }
}

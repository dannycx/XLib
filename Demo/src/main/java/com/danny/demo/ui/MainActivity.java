package com.danny.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.danny.demo.R;
import com.danny.xui.ItemSelectView;
import com.danny.xui.itemclick.ItemClickView;

public class MainActivity extends Activity {
    private Context mContext;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;

    private ItemSelectView mSelectView;
    private ItemClickView mClickView;
    private String[] mToastStyle;

    private AlertDialog mDialog;
    private int mStyleIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mContext=this;
        mParams=new WindowManager.LayoutParams();
        mWindowManager= (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        showToast();
    }

    private void initView() {
        mSelectView= (ItemSelectView) findViewById(R.id.item_select);
        mClickView= (ItemClickView) findViewById(R.id.item_click);
        mClickView.setTitle("");
        mToastStyle=new String[]{"透明","橙色","蓝色","灰色","绿色"};
        mClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mClickView.setDes(mToastStyle[0]);
                showToastStyleDialog();
            }
        });
    }

    private void showToastStyleDialog() {
        mDialog = new AlertDialog.Builder(mContext)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("设置对话框样式")
                .setSingleChoiceItems(mToastStyle, mStyleIndex, (dialog, i) -> {
                    mStyleIndex=i;
                    dialog.dismiss();
                    mClickView.setDes(mToastStyle[mStyleIndex]);
                })//选择单个条目监听
                .setNegativeButton("取消", (dialog, i) -> dialog.dismiss())//取消
                .create();
        mDialog.show();
    }

    //弹土司
    private void showToast() {
        mParams.width= WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mParams.format= PixelFormat.TRANSLUCENT;
        mParams.type= WindowManager.LayoutParams.TYPE_PHONE;//类型
        mParams.gravity= Gravity.START + Gravity.TOP;//位置
        mView=View.inflate(mContext,R.layout.custom_toast_view,null);
//        mView=LayoutInflater.from(mContext).inflate(R.layout.custom_toast_view,null);
        mWindowManager.addView(mView,mParams);
    }
}

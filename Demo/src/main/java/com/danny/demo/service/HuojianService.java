package com.danny.demo.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.danny.demo.R;
import com.danny.demo.ui.BackgroundActivity;

/**
 * 火箭服务
 * Created by danny on 2018/5/9.
 */
public class HuojianService extends Service {
    private WindowManager mWM;
    private int mScreenHeight;
    private int mScreenWidth;
    private View mHuoJian;
    private WindowManager.LayoutParams params;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int height= (int) msg.obj;
            params.y=height;
            mWM.updateViewLayout(mHuoJian,params);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取窗体对象
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

        mScreenHeight = mWM.getDefaultDisplay().getHeight();
        mScreenWidth = mWM.getDefaultDisplay().getWidth();
        showHuojian();
    }

    private void showHuojian() {
        params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	默认能够被触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE; //在响铃的时候显示吐司,和电话类型一致
        params.setTitle("Toast");

        //指定吐司的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT + Gravity.TOP;

        //吐司显示效果(吐司布局文件),xml-->view(吐司),将吐司挂在到windowManager窗体上
        mHuoJian = View.inflate(this, R.layout.huojian_toast, null);
        ImageView iv = mHuoJian.findViewById(R.id.huo_jian);
        AnimationDrawable drawable = (AnimationDrawable) iv.getBackground();

        drawable.start();
        //在窗体上挂在一个view(权限)
        mWM.addView(mHuoJian, params);

        mHuoJian.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        params.x = params.x + disX;
                        params.y = params.y + disY;

                        //容错处理
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > mScreenWidth - mHuoJian.getWidth()) {
                            params.x = mScreenWidth - mHuoJian.getWidth();
                        }
                        if (params.y > mScreenHeight - mHuoJian.getHeight() - 22) {
                            params.y = mScreenHeight - mHuoJian.getHeight() - 22;
                        }

                        //告知窗体吐司需要按照手势的移动,去做位置的更新
                        mWM.updateViewLayout(mHuoJian, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP://发射火箭
                        if(params.x>100 && params.x<200 && params.y>350){
                            sendHuoJian();
                            //开启尾气activity
                            Intent intent=new Intent(getApplicationContext(), BackgroundActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//外部开启activity需开辟新的任务栈
                            startActivity(intent);
                        }
                        break;
                }
                //true 响应拖拽触发的事件
                return true;
            }
        });
    }

    private void sendHuoJian() {
        //再向上移动过程中y轴一直减少
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主线程不可睡，阻塞
                for (int i=0;i<11;i++){
                    int height = 350-i*35;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg=Message.obtain();
                    msg.obj=height;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWM != null && mHuoJian != null) {
            mWM.removeView(mHuoJian);
        }
    }
}

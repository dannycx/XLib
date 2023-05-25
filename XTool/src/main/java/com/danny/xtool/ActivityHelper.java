package com.danny.xtool;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * activity管理类
 * @author danny
 * 2018/10/11.
 */

public class ActivityHelper {
    private static ActivityHelper instance = null;
    private long startTime = 0;

    private ActivityHelper() {}

    /** 单例对象 */
    public static ActivityHelper getInstance() {
        if (instance == null) {
            synchronized (ActivityHelper.class) {
                if (instance == null) {
                    instance = new ActivityHelper();
                }
            }
        }
        return instance;
    }



    /** 开机启动动画,AnimationDrawable动画图片资源回收 */
    public void recycleAnimationDrawable(AnimationDrawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.stop();
        for (int i = 0; i < drawable.getNumberOfFrames(); i++) {
            Drawable frame = drawable.getFrame(i);
            if (frame instanceof BitmapDrawable) {
                ((BitmapDrawable)frame).getBitmap().recycle();
            }
            frame.setCallback(null);
        }
        drawable.setCallback(null);
    }

}

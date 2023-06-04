package com.danny.xcamera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;

/**
 * 扩展相机支持缩放
 *
 * @author danny
 * @since 2020-11-02
 */
public class CameraXPreView extends PreviewView {
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private PreViewCallback callback;

    private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (null != callback) {
                callback.click(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (null != callback) {
                callback.doubleClick(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

    };

    private ScaleGestureDetector.OnScaleGestureListener scaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float delta = detector.getScaleFactor();
            if (null != callback) {
                callback.zoom(delta);
            }
            return super.onScale(detector);
        }
    };

    public CameraXPreView(@NonNull Context context) {
        this(context, null);
    }

    public CameraXPreView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraXPreView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(context, gestureListener);
        scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        if (!scaleGestureDetector.isInProgress()) {
            gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    public void setCallback(PreViewCallback callback) {
        this.callback = callback;
    }
}

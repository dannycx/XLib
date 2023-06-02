package com.danny.xui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by danny on 2018/12/3.
 * 将正方形图片显示为圆形
 */
public class CircularImage extends AppCompatImageView {
    private float roundness;
    private final float density;

    public CircularImage(Context context) {
        super(context);
        density = context.getResources().getDisplayMetrics().density;
        init();
    }

    public CircularImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        init();
    }

    public CircularImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = context.getResources().getDisplayMetrics().density;
        init();
    }

    @Override
    public void draw(Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();
        Bitmap composedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap originalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas composedCanvas = new Canvas(composedBitmap);
        Canvas originalCanvas = new Canvas(originalBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(-16777216);
        super.draw(originalCanvas);
        composedCanvas.drawARGB(0, 0, 0, 0);
        composedCanvas.drawRoundRect(new RectF(0.0f, 0.0f, (float) width, (float) height), this.roundness, this.roundness, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        composedCanvas.drawBitmap(originalBitmap, 0.0f, 0.0f, paint);
        canvas.drawBitmap(composedBitmap, 0.0f, 0.0f, new Paint());
    }

    public float getRoundness() {
        return this.roundness / this.density;
    }

    public void setRoundness(float roundness) {
        this.roundness = roundness * this.density;
    }

    private void init() {
        this.setRoundness(100.0f);
    }
}

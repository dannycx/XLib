package com.danny.xui.radar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

public class RadarChartUtil {
    public static final float BASE_FACTOR = 0.9f;
    public static final float BASE_LABEL_WIDTH_DP = 30f;
    private static Rect rect = new Rect();
    private static Paint.FontMetrics metrics = new Paint.FontMetrics();

    public static void drawXAxisValue(Canvas c, String formattedLabel, float x, float y, Paint paint, float angleDegrees, int position, int count) {
        float drawOffsetX = 0.0f;
        float drawOffsetY = 4.0f;
        float lineHeight = paint.getFontMetrics(metrics);
        paint.getTextBounds(formattedLabel, 0, formattedLabel.length(), rect);
        float factorX = (float) Math.cos(Math.toRadians(angleDegrees));
        float factorY = (float) Math.sin(Math.toRadians(angleDegrees));
        drawOffsetX += rect.width() * factorX * 0.5f;
        drawOffsetY += lineHeight * factorY * 0.5f;
        c.save();

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(paint.getTextSize());
        textPaint.setColor(paint.getColor());
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(paint.getTypeface());
        textPaint.setStyle(paint.getStyle());

        if (count == 7 && formattedLabel.length() > 8 && (position != 3 || position != 4 || position != 0)) {
            StaticLayout layout = new StaticLayout(formattedLabel, textPaint, 210
                    , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);

            if (position == 6 || position == 5) {
                x -= 210;
            }
            c.translate(x, y);
            layout.draw(c);
        } else if (count == 6 && formattedLabel.length() > 8 && (position != 0 || position != 3)) {
            StaticLayout layout = new StaticLayout(formattedLabel, textPaint, 210
                    , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
            if (position > 3) {
                x -= 210;
            }
            c.translate(x, y);
            layout.draw(c);
        } else if (count == 5 && formattedLabel.length() > 8 && (position == 1 || position == 4)) {
            StaticLayout layout = new StaticLayout(formattedLabel, textPaint, 165
                    , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
            if (position == 4) {
                x -= 165;
            }
            c.translate(x, y);
            layout.draw(c);
        } else if (count == 4 && formattedLabel.length() > 8 && (position == 1 || position == 3)) {
            StaticLayout layout = new StaticLayout(formattedLabel, textPaint, 160
                    , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);

            if (position == 3) {
                x -= 160;
            }
            c.translate(x, y);
            layout.draw(c);
        } else {
            c.translate(x, y);
            c.drawText(formattedLabel, drawOffsetX, drawOffsetY, paint);
        }

        c.restore();
    }
}

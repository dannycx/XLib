# Bitmap相关问题

## java.lang.IllegalArgumentException: can't create mutable bitmap with Config.HARDWARE
* canvas无法绘制hardware bitmap,转换为software bitmap
```
1.
/**
 * hardware bitmap 转换成 software bitmap
 */
public Bitmap convertSoftWareBitmap(Bitmap src) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || src.getConfig() != Bitmap.Config.HARDWARE) {
        return src;
    }
    final int w = src.getWidth();
    final int h = src.getHeight();

    // For hardware bitmaps, use the Picture API to directly create a software bitmap
    Picture picture = new Picture();
    Canvas canvas = picture.beginRecording(w, h);
    canvas.drawBitmap(src, 0, 0, null);
    picture.endRecording();
    return Bitmap.createBitmap(picture, w, h, Bitmap.Config.ARGB_8888);
}

2.
/**
 * hardware bitmap 转换成 software bitmap
 */
fun convertSoftWareBitmap(src: Bitmap): Bitmap {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || src.config != Bitmap.Config.HARDWARE) {
        return src
    }
    val w = src.width
    val h = src.height

    // For hardware bitmaps, use the Picture API to directly create a software bitmap
    val picture = Picture()
    val canvas = picture.beginRecording(w, h)
    canvas.drawBitmap(src, 0f, 0f, null)
    picture.endRecording()
    return Bitmap.createBitmap(picture, w, h, Bitmap.Config.ARGB_8888)
}

3.
Bitmap newBitmap = b.copy(Bitmap.Config.ARGB_8888, b.isMutable());
```

## android13截屏
```
import android.graphics.Bitmap;
import android.os.IBinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ScreenshotUtils {
    public static Bitmap screenshotAndroid13() {
        final IBinder displayToken = getInternalDisplayToken();
        final Object captureArgs = getCaptureArgs(displayToken);
        final Object screenshotBuffer = getScreenshotHardwareBuffer(captureArgs);
        final Bitmap screenShot = getAsBitmap(screenshotBuffer);
        LogUtils.INSTANCE.d("screenshotAndroid13 displayToken: " + displayToken + ", captureArgs: " + captureArgs
            + ", screenshotBuffer: " + screenshotBuffer + ", screenShot: " + screenShot);
        if (screenShot == null) {
            LogUtils.INSTANCE.w("Failed to take fullscreen screenshot");
            return null;
        }

        // Optimization
        screenShot.setHasAlpha(false);
        return screenShot;
    }

    private static IBinder getInternalDisplayToken() {
        String className = "android.view.SurfaceControl";
        try {
            Class<?> clz = Class.forName(className);
            Method method = clz.getMethod("getInternalDisplayToken");
            Object result = method.invoke(null);
            return (IBinder) result;
        } catch (Exception e) {
            LogUtils.INSTANCE.e("getInternalDisplayToken Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Object getCaptureArgs(IBinder displayToken) {
        String className = "android.view.SurfaceControl$DisplayCaptureArgs$Builder";
        try {
            Class<?> clz = Class.forName(className);
            Constructor con = clz.getDeclaredConstructor(IBinder.class);
            Object instance = con.newInstance(displayToken);
            Method method = clz.getMethod("build");
            Object result = method.invoke(instance, null);
            return result;
        } catch (Exception e) {
            LogUtils.INSTANCE.e("getCaptureArgs Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static Object getScreenshotHardwareBuffer(Object captureArgs) {
        String className = "android.view.SurfaceControl";
        String classParam = "android.view.SurfaceControl$DisplayCaptureArgs";
        try {
            Class<?> clz = Class.forName(className);
            Class<?> clzParam = Class.forName(classParam);
            Method method = clz.getMethod("captureDisplay", clzParam);
            Object screenshotBuffer = method.invoke(null, captureArgs);
            return screenshotBuffer;
        } catch (Exception e) {
            LogUtils.INSTANCE.e("getScreenshotHardwareBuffer Exception: " + e.getMessage());
            e.printStackTrace();

        }
        return null;
    }

    private static Bitmap getAsBitmap(Object screenshotBuffer) {
        if (screenshotBuffer == null) {
            return null;
        }
        String className = "android.view.SurfaceControl$ScreenshotHardwareBuffer";
        try {
            Class<?> clz = Class.forName(className);
            Method method = clz.getMethod("asBitmap");
            Object obj = method.invoke(screenshotBuffer);
            return (Bitmap) obj;
        } catch (Exception e) {
            LogUtils.INSTANCE.e("getAsBitmap Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
```

package com.danny.xcamera;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CameraXUtil {

    /**
     * 扫描媒体库并保存图片
     * @param context
     * @param photoFile
     */
    public static void mediaScan(Context context, File photoFile) {
        if (null == photoFile) {
            return;
        }

        String fileName = photoFile.getName();
        if (pathIsNull(fileName)) {
            return;
        }
        int index = fileName.lastIndexOf(".");
        if (-1 == index) {
            return;
        }
        String extension = fileName.substring(index + 1);
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        try {
            MediaScannerConnection.scanFile(context, new String[]{photoFile.getCanonicalPath()}, new String[]{mimeTypeFromExtension}, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean pathIsNull(String fileName) {
        return null == fileName && fileName.trim().isEmpty();
    }

    public static File getFile(Context context, boolean isSaveSdcard) {
        String fileDir = null;
        String fileName = null;
        if (isSaveSdcard) {
            fileDir = getExternalStorage();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
            fileName = "X_" + sdf.format(System.currentTimeMillis()) + ".jpg";
        } else {
            fileDir = getCache(context);
            File file = new File(fileDir, "X_Security_Picture.jpg");
            if (file.exists()) {
                boolean delete = file.delete();
                if (!delete) {
                    Log.e("TAG", "delete file failed");
                }
            }
            return file;
        }
        return new File(fileDir, fileName);
    }

    private static String getExternalStorage() {
        return Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "camere";
    }

    private static String getCache(Context context) {
        String pathDir;
        File file = new File(context.getFilesDir(), "cacheFile");
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                Log.e("TAG", "mkdirs failure");
            }
        }
        try {
            pathDir = file.getCanonicalPath();
        } catch (IOException e) {
            pathDir = getExternalStorage();
        }
        return pathDir;
    }
}

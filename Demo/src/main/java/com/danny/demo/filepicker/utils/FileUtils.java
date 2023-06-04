package com.danny.demo.filepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.danny.common.Constants;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    private static final String[][] mimeType = {
            // 图片
            {"bmp", "image/bmp"},
            {"gif", "image/gif"},
            {"png", "image/png"},
            {"jpg", "image/jpeg"},
            {"jpeg", "image/jpeg"},
            {"jpe", "image/jpeg"},
            // 文档
            {"doc", "application/msword"},
            {"docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"rtf", "application/rtf"},
            {"xls", "application/vnd.ms-excel application/x-excel"},
            {"xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"ppt", "application/vnd.ms-powerpoint"},
            {"pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {"pps", "application/vnd.ms-powerpoint"},
            {"ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow"},
            {"pdf", "application/pdf"},
            {"txt", "text/plain"},
            {"xml", "text/xml"},
            {"html", "text/html"},
            {"css", "text/css"},
            {"js", "text/javascript"},
            {"json", "text/plain"},
            // 视频
            {"mid", "audio/mid"},
            {"mp4", "audio/mp4"},
            {"midi", "audio/mid"},
            {"rmi", "audio/mid"},
            {"rmvb", "audio/x-pn-realaudio"},
            {"3gp", "video/3gpp"},
            // 音频
            {"wav", "audio/wav"},
            {"wma", "audio/x-ms-wma"},
            {"wmv", "video/x-ms-wmv"},
            {"mp3", "audio/mpeg"},
            {"mp2", "audio/mpeg"},
            {"mpe", "audio/mpeg"},
            {"mpeg", "audio/mpeg"},
            {"mpg", "audio/mpeg"},
            {"rm", "application/vnd.rn-realmedia"},
            // 压缩包
            {"zip", "application/x-zip-compressed"},
            {"apk", "application/vnd.android.package-archive"},
            {"", "*/*"}
    };

    public static void openFile(Context context, File file) {
        if (null == file || !file.exists()) {
            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        String extension = FilePickerUtil.getExtension(file);

        if (TextUtils.isEmpty(extension)) {
            Toast.makeText(context, "文件类型错误", Toast.LENGTH_SHORT).show();
            return;
        }

        String mimeType = findMimeType(extension);
        if (TextUtils.isEmpty(mimeType)) {
            Toast.makeText(context, "mimeType未知", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(getUri(context, file), mimeType);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "请下载相关软件", Toast.LENGTH_SHORT).show();
        }
    }

    private static Uri getUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, "com.x.xbase.fileprovider", file);
        }
        return Uri.fromFile(file);
    }

    private static String findMimeType(String extension) {
        String type = "";
        for (String[] strings : mimeType) {
            if (extension.equals(strings[0])) {
                type = strings[1];
                break;
            }
        }
        return type;
    }

    /**
     * 删除文件
     */
    public static boolean delete(File file) {
        return null != file && (!file.exists() || (file.isFile() && file.delete()));
    }

    /**
     * 删除文件
     * @param file 文件
     * @return 成功true
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }

        if(file.isDirectory()){
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return file.delete();
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            return file.delete();
        }
        return false;
    }

    /** 文件流转为字符串 */
    public static String stream2String(InputStream is) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] bys = new byte[1024];
            int len = -1;
            while ((len = is.read(bys)) != -1) {
                baos.write(bys, 0, len);
            }
            close(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }

    /** 关闭流,避免多处抛异常 */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, e.toString());
                return false;
            }
        }
        return true;
    }

}

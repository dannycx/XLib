package com.danny.demo.filepicker.utils;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.danny.demo.filepicker.FilePickerConstants;
import com.danny.demo.filepicker.bean.PickerFile;
import com.danny.xtool.LogTool;
import com.danny.xtool.UiTool;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 文件工具类
 *
 * @author danny
 */
public class FilePickerUtil {
    private static final String[] SIZE_UNIT = {"B", "KB", "M", "G", "T"};
    private static final int FILE_BASIC = 1024;

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static final int TIME_SECOND = 1000;
    private static final int TIME_MINUTE = 60;
    private static final int TIME_HOUR = 3600;

    private static final String STRING_ZERO = "00:00";
    private static final String STRING_SECOND = "00:%02d";
    private static final String STRING_MINUTE = "%02d:%02d";
    private static final String STRING_HOUR = "%02d:%02d:%02d";

    private static final String PATH_WECHAT_1 = "/tencent/MicroMsg/Download/";
    private static final String PATH_WECHAT_2 = "/tencent/MicroMsg/WeiXin/";
//    private static final String PATH_WECHAT_2 = "/Pictures/weixin";
//    private static final String PATH_WECHAT_3 = "/Android/data/com.tencent.mm";// 微信缓存
    private static final String[] PATH_WECHAT = {PATH_WECHAT_1, PATH_WECHAT_2};

    private static final String PATH_QQ_1 = "/tencent/QQ_Images";
    private static final String PATH_QQ_2 = "/tencent/QQfile_recv";
//    private static final String PATH_QQ_3 = "/tencent/MobileQQ";
//    private static final String PATH_QQ_4 = "/Android/data/com.tencent.Mobileqq";// QQ缓存
    private static final String[] PATH_QQ = {PATH_QQ_1, PATH_QQ_2};

    public static final String[] CACHE_ROOT_PATH = {
            "/storage/emulated/0/Android"
            , "/storage/emulated/0/ANRSnap"
            , "/storage/emulated/0/backup"
            , "/storage/emulated/0/backups"
            , "/storage/emulated/0/HuaweiBackup"
            , "/storage/emulated/0/tmp"
            , "/storage/emulated/0/system"};// 缓存路径

    /**
     * 查询我的应用路径
     *
     * @param basePath 根路径
     * @param categories 类型,长度为0,查所有
     * @return 路径
     */
    public static List<File> getAppDirs(String basePath, List<String> categories) {
        List<File> list = new ArrayList<>();
        boolean addWeChat = categories.size() == 0 || categories.contains(FilePickerConstants.CATEGORY_WECHAT);
        if (addWeChat) {
            addFile(basePath, PATH_WECHAT, list);
        }
        boolean addQQ = categories.size() == 0 || categories.contains(FilePickerConstants.CATEGORY_QQ);
        if (addQQ) {
            addFile(basePath, PATH_QQ, list);
        }
        return list;
    }

    private static void addFile(String basePath, String[] paths, List<File> list) {
        for (String path : paths) {
            File file = new File(basePath + path);
            list.add(file);
        }
    }

    public static String getCategory(File file) {
        return getCategory(getExtension(file));
    }

    /**
     * 获取文件分类
     * @param extension 扩展名
     * @return 分类:image,docs,zip,audio,video,qq,wechat
     */
    private static String getCategory(String extension) {
        if (TextUtils.isEmpty(extension)) {
            return "";
        }
        extension = extension.toLowerCase();
        String category = "";
        if (FilePickerConstants.CATEGORY_TYPE.containsKey(extension)) {
            category = FilePickerConstants.CATEGORY_TYPE.get(extension);
        }
        return category;
    }

    public static String getExtension(File file) {
        return null != file ? getExtension(file.getName()) : "";
    }

    /**
     * 获取文件扩展名
     * @param name 文件名
     * @return 扩展名
     */
    private static String getExtension(String name) {
        String extension = "";
        if (!TextUtils.isEmpty(name)) {
            String[] splits = name.split("\\.");
            int length = splits.length;
            extension = length >= 2 ? splits[length - 1] : "";
        }
        return extension;
    }

    public static int getDuration(File file) throws IOException {
        int duration = 0;
        if (isMediaPlayer(file)) {
            MediaPlayer media = new MediaPlayer();
            media.setDataSource(file.getAbsolutePath());
            media.prepare();
            duration = media.getDuration();
            media.release();
        }
        return duration;
    }

    public static String getDurationText(int duration) {
        int second = duration / TIME_SECOND;
        if (second <= 0) {
            return STRING_ZERO;
        } else if (second < TIME_MINUTE) {
            return String.format(Locale.getDefault(), STRING_SECOND, second % TIME_MINUTE);
        } else if (second < TIME_HOUR) {
            return String.format(Locale.getDefault(), STRING_MINUTE, second / TIME_MINUTE
                    , second % TIME_MINUTE);
        } else {
            return String.format(Locale.getDefault(), STRING_HOUR, second / TIME_HOUR
                    , second % TIME_HOUR / TIME_MINUTE, second % TIME_MINUTE);
        }
    }

    public static String getDurationText(File f) {
        int duration;
        try {
            duration = getDuration(f);
        } catch (IOException e) {
            duration = 0;
        }
        return getDurationText(duration);
    }

    public static String getFileSize(long length) {
        double fileSize = length;
        int index = 0;
        while (fileSize >= FILE_BASIC && index < SIZE_UNIT.length - 1) {
            index ++;
            fileSize /= FILE_BASIC;
        }
        if (index >= SIZE_UNIT.length) {
            index = SIZE_UNIT.length -1;
        }
        fileSize = new BigDecimal(fileSize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return fileSize + SIZE_UNIT[index];
    }

    public static String getLastModify(File file)  {
        String lastModifyResult = "";
        String curYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE, Locale.getDefault());
        lastModifyResult = sdf.format(file.lastModified());
        if (lastModifyResult.substring(0, 4).equals(curYear)) {
            lastModifyResult = lastModifyResult.substring(5);
        }
        return lastModifyResult;
    }

    public static boolean isMediaPlayer(File file) {
        return isMediaPlayer(getCategory(file));
    }

    private static boolean isMediaPlayer(String category) {
        return FilePickerConstants.CATEGORY_AUDIO.equals(category)
                || FilePickerConstants.CATEGORY_VIDEO.equals(category);
    }

    public static String fileSize (ArrayList<PickerFile> select, ArrayList<String> selected) {
        if (UiTool.INSTANCE.isEmpty(select) && UiTool.INSTANCE.isEmpty(selected)) {
            return "0B";
        }
        try {
            double selectSize = 0;
            if (!UiTool.INSTANCE.isEmpty(select)) {
                for (PickerFile f : select) {
                    selectSize += fileSize(f.getSize());
                }
            }
            double selectedSize = 0;
            if (!UiTool.INSTANCE.isEmpty(selected)) {
                for (String size : selected) {
                    selectedSize += fileSize(size);
                }
            }
            return calculateSize(selectSize + selectedSize);
        } catch (Exception e) {
            LogTool.INSTANCE.e(e.toString());
        }
        return "0B";
    }

    private static String calculateSize(double size) {
        if (size < 1024) {
            return formatSize(size) + "B";
        } else if (size < Math.pow(1024, 2)) {
            return formatSize(size / 1024) + "KB";
        } else if (size < Math.pow(1024, 3)) {
            return formatSize(size / Math.pow(1024, 2)) + "M";
        } else if (size < Math.pow(1024, 4)) {
            return formatSize(size / Math.pow(1024, 3)) + "G";
        } else if (size < Math.pow(1024, 5)) {
            return formatSize(size / Math.pow(1024, 4)) + "T";
        }
        return "0B";
    }

    private static double formatSize(double fileSize) {
        return new BigDecimal(fileSize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * B-M
     * @param fileSize 文件大小-B
     * @return M
     */
    private static double convertM(double fileSize) {
        return fileSize / Math.pow(1024, 2);
    }

    private static double fileSize(String fileSize) {
        if (TextUtils.isEmpty(fileSize)) {
            return 0;
        } else if (fileSize.endsWith("KB")) {
            return Double.parseDouble(fileSize.split("KB")[0].trim()) * 1024;
        } else if (fileSize.endsWith("M")) {
            return Double.parseDouble(fileSize.split("M")[0].trim()) * Math.pow(1024, 2);
        } else if (fileSize.endsWith("G")) {
            return Double.parseDouble(fileSize.split("G")[0].trim()) * Math.pow(1024, 3);
        } else if (fileSize.endsWith("T")) {
            return Double.parseDouble(fileSize.split("T")[0].trim()) * Math.pow(1024, 4);
        } else if (fileSize.endsWith("B")) {
            return Double.parseDouble(fileSize.split("B")[0].trim());
        }
        return 0;
    }

}

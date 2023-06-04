package com.danny.demo.filepicker;

import com.danny.demo.filepicker.utils.FilePickerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilePickerConstants {
    public static final int TYPE_FILE = 1;
    public static final int TYPE_APP = 2;
    public static final int EVENT_CODE_ITEM_CLICK = 100;
    public static final int EVENT_CODE_CHECK = 101;
    public static final int EVENT_CODE_DELETE = 102;

    /**
     * 图片打开路径key
     */
    public static final String PREVIEW_IMAGE_KEY = "PREVIEW_IMAGE_KEY";

    /**
     * 文件类型key:1-文件 2-常用应用
     */
    public static final String FILE_TYPE_KEY = "FILE_TYPE_KEY";

    /**
     * 文件种类key:image,docs,zip,other,wechart,qq,all
     */
    public static final String FILE_CATEGORY_KEY = "FILE_CATEGORY_KEY";

    /**
     * 已选择文件key
     */
    public static final String FILE_SELECTED_KEY = "FILE_SELECTED_KEY";

    /**
     * 上传文件url key
     */
    public static final String FILE_UPLOAD_KEY = "FILE_UPLOAD_KEY";

    /**
     * 文件个数选择key
     */
    public static final String FILE_COUNT_KEY = "FILE_COUNT_KEY";

    /**
     * 文件个数选择默认6个
     */
    public static final int FILE_COUNT_VALUE = 6;

    /**
     * 文件大小限制key
     */
    public static final String FILE_SIZE_KEY = "FILE_SIZE_KEY";

    /**
     * 文件大小限制默认200M
     */
    public static final long FILE_SIZE_VALUE = 200 * 1024 * 1024;

    public static final String EVENT_INCREMENT = "+";
    public static final String EVENT_REDUCE = "-";

    public static final String CATEGORY_IMAGE = "image";
    public static final String CATEGORY_DOCS = "docs";
    public static final String CATEGORY_ZIP = "zip";
    public static final String CATEGORY_APK = "apk";
    public static final String CATEGORY_AUDIO = "audio";
    public static final String CATEGORY_VIDEO = "video";
    public static final String CATEGORY_OTHER = "other";
    public static final String CATEGORY_ALL = "all";
    public static final String CATEGORY_WECHAT = "wechat";
    public static final String CATEGORY_QQ = "qq";

    public static final String[] EXTENSION_IMAGE = {"png", "jpg", "jpeg", "webp", "bmp", "heic", "heif"};
    public static final String[] EXTENSION_DOCS = {"txt", "doc", "docx", "xls", "xlsx", "pdf", "xml", /*"log",*/ "json", "htm", "html", "ppt", "pptx"};
    public static final String[] EXTENSION_ZIP = {"zip"};
    public static final String[] EXTENSION_AUDIO = {"mp3", "mkv", "m4a", "m4a", "aac", "flac", "gsm", "mid", "xmf", "mxmf", "rtttl", "rtx", "ota", "imy", "wav", "ogg"};
    public static final String[] EXTENSION_VIDEO = {"mp4", "3gp", "mkv", "ts", "webm"};
    public static final String[] EXTENSION_APK = {"apk"};

    public static final Map<String, String> CATEGORY_TYPE = new HashMap<>();

    public static final ArrayList<String> excludePath = new ArrayList<>(7);

    static {
        for (String key : EXTENSION_APK) {
            CATEGORY_TYPE.put(key, CATEGORY_APK);
        }

         for (String key : EXTENSION_VIDEO) {
            CATEGORY_TYPE.put(key, CATEGORY_VIDEO);
        }

         for (String key : EXTENSION_AUDIO) {
            CATEGORY_TYPE.put(key, CATEGORY_AUDIO);
        }

         for (String key : EXTENSION_ZIP) {
            CATEGORY_TYPE.put(key, CATEGORY_ZIP);
        }

         for (String key : EXTENSION_DOCS) {
            CATEGORY_TYPE.put(key, CATEGORY_DOCS);
        }

         for (String key : EXTENSION_IMAGE) {
            CATEGORY_TYPE.put(key, CATEGORY_IMAGE);
        }

        for (String path : FilePickerUtil.CACHE_ROOT_PATH) {
            excludePath.add(path);
        }
    }
}

package com.danny.demo.update.bean;

import java.io.Serializable;

public class DownloadEntity implements Serializable {
    private String title;
    private String content;
    private String apkUrl;
    private String md5;
    private String versionCode;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public String getMd5() {
        return md5;
    }

    public String getVersionCode() {
        return versionCode;
    }
}

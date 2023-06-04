package com.danny.demo.filepicker.bean;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.danny.demo.filepicker.FilePickerDurationTask;
import com.danny.demo.filepicker.listener.FilePickerDurationCallback;
import com.danny.demo.filepicker.utils.FilePickerUtil;

import java.io.File;
import java.io.IOException;

public class PickerFile implements Comparable<PickerFile> {
    private File file;
    private String fileName;
    private String category;
    private String extension;
    private String lastModify;
    private String size;
    private String duration;
    private boolean isSelect;
    private boolean hasOperated;

    public PickerFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        if (null == fileName) {
            fileName = file.getName();
        }
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCategory() {
        if (null == category) {
            category = FilePickerUtil.getCategory(file);
        }
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExtension() {
        if (null == extension) {
            extension = FilePickerUtil.getExtension(file);
        }
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getLastModify() {
        if (TextUtils.isEmpty(lastModify)) {
            lastModify = FilePickerUtil.getLastModify(file);
        }
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public String getSize() {
        if (TextUtils.isEmpty(size)) {
            size = FilePickerUtil.getFileSize(file.length());
        }
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void getDuration(FilePickerDurationCallback callback) {
        new FilePickerDurationTask(this, callback).execute();
    }

    public int getDuration() {
        int duration = 0;
        try {
            duration = FilePickerUtil.getDuration(file);
        } catch (IOException e) {
            duration = 0;
        }
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isHasOperated() {
        return hasOperated;
    }

    public void setHasOperated(boolean hasOperated) {
        this.hasOperated = hasOperated;
    }

    @Override
    public int hashCode() {
        return null == file ? super.hashCode() : file.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof PickerFile) {
            PickerFile otherInstance = (PickerFile) obj;
            return file == otherInstance.file;
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(PickerFile o) {
        long var1 = file.lastModified();
        long var2 = o.file.lastModified();
        if (var1 < var2) {
            return 1;
        } else if (var1 > var2){
            return -1;
        }
        return 0;
    }
}

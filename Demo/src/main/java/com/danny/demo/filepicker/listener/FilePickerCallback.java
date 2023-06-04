package com.danny.demo.filepicker.listener;

import com.danny.demo.filepicker.bean.PickerFile;

import java.util.ArrayList;

public interface FilePickerCallback {
    void onResponse(ArrayList<PickerFile> var);
    void onFailure(Throwable t);
}

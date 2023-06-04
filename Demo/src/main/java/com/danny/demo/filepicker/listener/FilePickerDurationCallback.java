package com.danny.demo.filepicker.listener;

public interface FilePickerDurationCallback {
    void onResponse(int duration, String durationStr);
    void onFailure(Throwable t);
}

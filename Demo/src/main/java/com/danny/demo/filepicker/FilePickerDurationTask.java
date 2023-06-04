package com.danny.demo.filepicker;

import android.os.AsyncTask;

import com.danny.demo.filepicker.bean.PickerFile;
import com.danny.demo.filepicker.listener.FilePickerDurationCallback;
import com.danny.demo.filepicker.utils.FilePickerUtil;

import java.io.IOException;

/**
 * @author danny
 * 获取媒体音频问题时间的
 */
public class FilePickerDurationTask extends AsyncTask<Void, Void, Integer> {
    private PickerFile pickerFile;
    private FilePickerDurationCallback callback;
    private Exception err;

    public FilePickerDurationTask(PickerFile pickerFile, FilePickerDurationCallback callback) {
        this.pickerFile = pickerFile;
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int duration = 0;
        err = null;
        try {
            duration = FilePickerUtil.getDuration(pickerFile.getFile());
        } catch (IOException e) {
            err = e;
        }
        return duration;
    }

    @Override
    protected void onPostExecute(Integer duration) {
        super.onPostExecute(duration);
        if (null != err) {
            callback.onFailure(err);
        } else {
            callback.onResponse(duration, FilePickerUtil.getDurationText(duration));
        }
    }

}

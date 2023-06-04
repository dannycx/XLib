package com.danny.demo.filepicker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.danny.demo.filepicker.bean.PickerFile;
import com.danny.demo.filepicker.listener.FilePickerCallback;
import com.danny.demo.filepicker.listener.FilePickerDeleteCallback;
import com.danny.demo.filepicker.listener.IFilePicker;
import com.danny.demo.filepicker.utils.FilePickerUtil;
import com.danny.demo.filepicker.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 文件收集器
 */
public class FilePicker implements IFilePicker {
    private static final String TAG = FilePicker.class.getSimpleName();

    public FilePicker() {}
    public FilePicker(Context context) {}

    @Override
    public XFilePickerEngine prepare() {
        return new XFilePickerEngine();
    }

    public static class XFilePickerEngine {
        private int type;
        private List<String> categories;

        public XFilePickerEngine() {
            type = FilePickerConstants.TYPE_FILE;
            categories = new ArrayList<>();
        }

        public XFilePickerEngine setType(int type) {
            this.type = type;
            return this;
        }

        /**
         * @param c 为空加载所有类型
         */
        public XFilePickerEngine setCategories(String[] c) {
            categories.clear();
            categories.addAll(Arrays.asList(c));
            return this;
        }

        public void getList(FilePickerCallback callback) {
            File externalDirectory = Environment.getExternalStorageDirectory();
            File[] dirs = new File[]{externalDirectory};
            if (FilePickerConstants.TYPE_APP == type) {
                List<File> xPickerFiles = FilePickerUtil.getAppDirs(externalDirectory.getPath(), categories);
                File[] files = new File[xPickerFiles.size()];
                dirs = xPickerFiles.toArray(files);
            }
            new FilePickerTask(this, callback, dirs).execute();
        }

        public void deleteFile(File file, FilePickerDeleteCallback callback) {
            callback.onResponse(FileUtils.delete(file));
        }

        public void deleteFile(PickerFile pickerFile, FilePickerDeleteCallback callback) {
            deleteFile(pickerFile.getFile(), callback);
        }

        @WorkerThread
        public void reCursorFile(File f, List<PickerFile> list) {
            File[] files = f.listFiles();
            if (null == files) {
                return;
            }
            for (File file: files) {
                if (file.isDirectory()) {
                    if (excludePath(file)) {
                        continue;
                    }
//                    if (list.size() > 50) {// todo:调试
//                        break;
//                    }
                    reCursorFile(file, list);
                } else {
                    if (FilePickerConstants.TYPE_FILE == type) {
                        if (categories.size() == 0) {// 添加所有
                            addToFiles(file, list);
                        } else {
                            String category = FilePickerUtil.getCategory(file);
                            if (categories.contains(category)) {
                                addToFiles(file, list);
                            }
                        }
                    } else if (FilePickerConstants.TYPE_APP == type) {
                        addToFiles(file, list);
                    }
                }
            }
        }

        private boolean excludePath(File file) {
            boolean exclude = false;
            try {
                exclude = FilePickerConstants
                        .excludePath.contains(file.getCanonicalPath()) || file.getCanonicalPath().contains("/.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return exclude;
        }

        private void addToFiles(File file, List<PickerFile> list) {
            String category = FilePickerUtil.getCategory(file);
            if (!TextUtils.isEmpty(category)) {
                PickerFile pickerFile = new PickerFile(file);
                list.add(pickerFile);
            }
        }

    }

    static class FilePickerTask extends AsyncTask<Void, Void, ArrayList<PickerFile>> {
        private File[] dirs;
        private XFilePickerEngine engine;
        private FilePickerCallback callback;

        public FilePickerTask(XFilePickerEngine engine, FilePickerCallback callback, File... dirs) {
            this.engine = engine;
            this.callback = callback;
            this.dirs = Arrays.copyOf(dirs, dirs.length);
        }

        @Override
        protected ArrayList<PickerFile> doInBackground(Void... voids) {
            long start = System.currentTimeMillis();
            ArrayList<PickerFile> result = new ArrayList<>();
            for (File file: dirs) {
                engine.reCursorFile(file, result);
            }
            Log.e(TAG, "aaaaaaaaaaaaa: " + result.size());
            Collections.sort(result);
            long duration = System.currentTimeMillis() - start;
            Log.i(TAG, "get file list duration : " + duration);
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<PickerFile> xPickerFiles) {
            super.onPostExecute(xPickerFiles);
            callback.onResponse(xPickerFiles);
        }
    }
}

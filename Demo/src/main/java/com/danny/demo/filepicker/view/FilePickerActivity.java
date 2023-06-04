package com.danny.demo.filepicker.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.danny.common.BaseResponse;
import com.danny.demo.R;
import com.danny.demo.databinding.ActivityFileBinding;
import com.danny.demo.filepicker.FilePicker;
import com.danny.demo.filepicker.FilePickerConstants;
import com.danny.demo.filepicker.bean.PickerFile;
import com.danny.demo.filepicker.utils.FilePickerUtil;
import com.danny.xbase.module.X;
import com.danny.xtool.LogTool;
import com.danny.xtool.StatusBarTool;
import com.danny.xtool.StringTool;
import com.danny.xtool.UiTool;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FilePickerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FilePickerActivity";
    private ActivityFileBinding fileBinding;
    private List<FilePickerFragment> fileFragments;
    private List<FilePickerFragment> appFragments;
    private int fileCount = 6;
    private long limitFileSize = 200 * 1024 * 1024;
    private String uploadUrl;
    private List<PickerFile> selectedFiles;
    private ArrayList<String> selectedPaths;
    private ArrayList<String> selectedSize;
    private ArrayList<PickerFile> selectList;
    private int selectedCount = 0;
    private int type = 1;
    private int curTab = 0;
    private boolean first = false;
    private long DELAY = 500L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileBinding = DataBindingUtil.setContentView(this, R.layout.activity_file);
        initData();
        initView();
        initTab();
        initListener();
        StatusBarTool.setStatusBarColor(this,
            UiTool.INSTANCE.getColor(this, com.danny.common.R.color.color_ffffff), true);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initView() {
        selectedFiles = new ArrayList<>(fileCount);
        if (UiTool.INSTANCE.isEmpty(selectedSize)) {

        } else {

        }
    }

    private void initListener() {
        fileBinding.xFileRoot.setOnClickListener(this::onClick);
        fileBinding.xFileSwitch.setOnClickListener(this::onClick);
        fileBinding.xFileSelect.setOnClickListener(this::onClick);
        fileBinding.xFileSize.setOnClickListener(this::onClick);
        fileBinding.xFileApp.setOnClickListener(this::onClick);
        fileBinding.xFileFile.setOnClickListener(this::onClick);
        fileBinding.xFileUpload.setOnClickListener(this::onClick);

        fileBinding.xFileTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                needHindTabSwitch();
                if (FilePickerConstants.TYPE_FILE == type) {
                    switchTab(tab.getPosition(), fileFragments);
                } else {
                    switchTab(tab.getPosition(), appFragments);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void needHindTabSwitch() {
        if (View.VISIBLE == fileBinding.xFileFunLayout.getVisibility()) {
            fileBinding.xFileFunLayout.setVisibility(View.GONE);
        }
    }

    private void switchTab(int position, List<FilePickerFragment> fragments) {
        if (first) {
            first = false;
            return;
        }
        FilePickerFragment curFragment = fragments.get(curTab);
        FilePickerFragment targetFragment = fragments.get(position);

        if (targetFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(curFragment)
                    .show(targetFragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(curFragment)
                    .add(R.id.x_file_contains, targetFragment)
                    .commit();
        }
        curTab = position;
        fileBinding.xFileTab.postDelayed(() -> targetFragment.selectFileAsync(selectList), DELAY);
    }

    private void switchTabTwo(int position, List<FilePickerFragment> oldPage, List<FilePickerFragment> newPage) {
        if (null == oldPage || null == newPage) {
            return;
        }
        if (first) {
            first = false;
            return;
        }

        FilePickerFragment curFragment = oldPage.get(curTab);
        FilePickerFragment targetFragment = newPage.get(position);

        if (targetFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(curFragment)
                    .show(targetFragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(curFragment)
                    .add(R.id.x_file_contains, targetFragment)
                    .commit();
        }
        curTab = position;
        fileBinding.xFileTab.postDelayed(() -> targetFragment.selectFileAsync(selectList), DELAY);
    }

    private void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            fileCount = intent.getIntExtra(FilePickerConstants.FILE_COUNT_KEY, FilePickerConstants.FILE_COUNT_VALUE);
            limitFileSize = intent.getLongExtra(FilePickerConstants.FILE_SIZE_KEY, FilePickerConstants.FILE_SIZE_VALUE);
            uploadUrl = intent.getStringExtra(FilePickerConstants.FILE_UPLOAD_KEY);
            String selectedFile = intent.getStringExtra(FilePickerConstants.FILE_SELECTED_KEY);

            if (!TextUtils.isEmpty(selectedFile)) {
                try {
                    selectedFiles = X.gson().fromJson(selectedFile, new TypeToken<List<FilePicker>>(){}.getType());
                    if (!UiTool.INSTANCE.isEmpty(selectedFiles)) {
                        selectedPaths = new ArrayList<>();
                        selectedSize = new ArrayList<>();
                        for (PickerFile f : selectedFiles) {
                            if (null == f || null == f.getFile()) {
                                continue;
                            }
                            selectedPaths.add(f.getFile().getAbsolutePath());
                            selectedSize.add(f.getSize());
                        }
                        selectedCount = null == selectedSize ? 0 : selectedSize.size();
                    }
                } catch (JsonSyntaxException e) {
                    LogTool.INSTANCE.d(e.toString());
                }
            }
        }
        selectList = new ArrayList<>(fileCount);
        initPage();
    }

    private void initPage() {
        if (null == selectedSize || 0 == selectedSize.size()) {
            fileBinding.xFileSize.setText(getString(R.string.x_file_size,
                FilePickerUtil.fileSize(selectList, selectedSize)));
            fileBinding.xFileSelect.setText(getString(R.string.x_file_count,
                selectList.size() + selectedCount));
        } else {
            fileBinding.xFileSize.setText(getString(R.string.x_file_size,
                FilePickerUtil.fileSize(selectList, selectedSize)));
            fileBinding.xFileSelect.setText(getString(R.string.x_file_count,
                selectList.size() + selectedCount));
        }
    }

    private void initTab() {
        fileBinding.xFileTab.removeAllTabs();
        String[] fileTab = UiTool.INSTANCE.getStringArray(this, R.array.x_file_tab);
        for (int i = 0; i < fileTab.length; i++) {
            fileBinding.xFileTab.addTab(fileBinding.xFileTab.newTab().setText(fileTab[i]), i == 0);
        }
        if (UiTool.INSTANCE.isEmpty(fileFragments)) {
            fileFragments = new ArrayList<>(4);
            fileFragments.add(FilePickerFragment.Companion.newInstance(type, FilePickerConstants.CATEGORY_IMAGE, selectedPaths));
            fileFragments.add(FilePickerFragment.Companion.newInstance(type, FilePickerConstants.CATEGORY_DOCS, selectedPaths));
            fileFragments.add(FilePickerFragment.Companion.newInstance(type, FilePickerConstants.CATEGORY_ZIP, selectedPaths));
            fileFragments.add(FilePickerFragment.Companion.newInstance(type, FilePickerConstants.CATEGORY_OTHER, selectedPaths));
            getSupportFragmentManager().beginTransaction().add(R.id.x_file_contains, fileFragments.get(0)).commitAllowingStateLoss();
            curTab = 0;
        } else {
            switchTabTwo(0, appFragments, fileFragments);
        }
    }

    private void initAppTab() {
        fileBinding.xFileTab.removeAllTabs();
        String[] appTab = UiTool.INSTANCE.getStringArray(this, R.array.x_app_tab);
        if (StringTool.INSTANCE.isEmpty(appTab)) {
            return;
        }
        for (int i = 0; i < appTab.length; i++) {
            fileBinding.xFileTab.addTab(fileBinding.xFileTab.newTab().setText(appTab[i]), i == 0);
        }
        if (UiTool.INSTANCE.isEmpty(appFragments)) {
            appFragments = new ArrayList<>(3);
            appFragments.add(FilePickerFragment.Companion.newInstance(type, FilePickerConstants.CATEGORY_ALL, selectedPaths));
            appFragments.add(FilePickerFragment.Companion.newInstance(type, FilePickerConstants.CATEGORY_WECHAT, selectedPaths));
            appFragments.add(FilePickerFragment.Companion.newInstance(type, FilePickerConstants.CATEGORY_QQ, selectedPaths));
        }
        switchTabTwo(0, fileFragments, appFragments);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.x_file_app == id) {
            fileBinding.xFileFunLayout.setVisibility(View.GONE);
            fileBinding.xFileSwitch.setText(getString(R.string.x_file_switch_app));
            switchType(FilePickerConstants.TYPE_APP);
        } else if (R.id.x_file_file == id) {
            fileBinding.xFileSwitch.setText(getString(R.string.x_file_switch_app));
            fileBinding.xFileFunLayout.setVisibility(View.GONE);
            switchType(FilePickerConstants.TYPE_FILE);
        } else if (R.id.x_file_switch == id) {
            fileBinding.xFileFunLayout.setVisibility(View.VISIBLE);
        } else if (R.id.x_file_upload == id) {

        } else if (R.id.x_file_select == id || R.id.x_file_size == id) {

        } else {

        }
    }

    private void switchType(int fileType) {
        type = fileType;
        first = true;
        if (FilePickerConstants.TYPE_FILE == fileType) {
            initTab();
        } else {
            initAppTab();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void itemEvent(BaseResponse message) {
        int eventCode = message.getCode();
        if (FilePickerConstants.EVENT_CODE_ITEM_CLICK == eventCode) {

        } else if (FilePickerConstants.EVENT_CODE_CHECK == eventCode) {
            String flag = message.getMessage();
            PickerFile var1 = (PickerFile) message.getResult();
            if (null == var1) {
                return;
            }
            if (FilePickerConstants.EVENT_INCREMENT.equals(flag)) {
                selectList.add(var1);
            } else {
                if (null == var1.getFile() || TextUtils.isEmpty(var1.getFile().getAbsolutePath())) {
                    return;
                }
                String filePath = var1.getFile().getAbsolutePath();
                for (int i = 0; i < selectList.size(); i++) {
                    PickerFile var2 = selectList.get(i);
                    if (null == var2 || null == var2.getFile()) {
                        continue;
                    }
                    if (filePath.equals(var2.getFile().getAbsolutePath())) {
                        selectList.remove(i);
                        break;
                    }
                }
            }
            initPage();
        }
    }
}

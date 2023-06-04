package com.danny.demo.update.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.danny.demo.update.AppUpdate;
import com.danny.demo.update.bean.DownloadEntity;
import com.danny.demo.update.net.INetDownloadCallback;

import java.io.File;

public class UpdateDialog extends DialogFragment {
    private static final String KEY ="download";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // 拿到dialog去黑边
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void show(FragmentActivity activity, DownloadEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, entity);
        UpdateDialog dialog = new UpdateDialog();
        dialog.setArguments(bundle);
        dialog.show(activity.getSupportFragmentManager(), "");
    }

    private void update() {
        AppUpdate.getInstance().getManager().download("", new File(getActivity().getCacheDir(), "target.apk"), new INetDownloadCallback() {
            @Override
            public void success(File apkFile) {
                // 安装
                dismiss();
//                SystemUtil.getFileMd5(apkFile);
//                SystemUtil.install(getActivity(), apkFile);
            }

            @Override
            public void progress(int progress) {
                // 显示进度
            }

            @Override
            public void failed(Throwable throwable) {
                // 下载失败
                dismiss();
            }
        }, UpdateDialog.this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        AppUpdate.getInstance().getManager().cancel(UpdateDialog.this);
    }
}

package com.danny.xui.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danny.xui.R;

/**
 * Created by danny on 2018/11/6.
 * 结果对话框
 */
public class ResultDialog extends DialogFragment {

    public static ResultDialog newInstance() {
        ResultDialog dialog = new ResultDialog();

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_result_layout, container, false);
        return view;
    }
}

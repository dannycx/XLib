package com.danny.demo.home;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;

import androidx.annotation.Nullable;

import com.danny.xbase.base.BaseActivity;

public class TextActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        InputFilter i = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (true) {
                    return "";
                }
                return null;
            }
        };
    }
}

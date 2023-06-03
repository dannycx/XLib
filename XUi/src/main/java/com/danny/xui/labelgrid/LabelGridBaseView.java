package com.danny.xui.labelgrid;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.danny.xui.R;
import com.danny.xui.databinding.LabelGridBaseBinding;
import com.danny.xui.labelgrid.bean.LabelGridBean;

public class LabelGridBaseView extends LinearLayout {
    private LabelGridBaseBinding binding;

    public LabelGridBaseView(Context context) {
        this(context, null);
    }

    public LabelGridBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelGridBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.label_grid_base, this, false);
    }

    public void setData(LabelGridBean.LabelGrid data) {
        if (data == null) {
            return;
        }
        binding.title.setText(data.getTitle());
        binding.name.setText(data.getName());
        binding.data.setText(data.getValue());
    }


}

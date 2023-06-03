package com.danny.xui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * 左侧带扫码右侧带删除按钮的EditText
 * Created by 75955 on 2018/8/15.
 */
public class GenerateEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    private Drawable clearDrawable;// 右侧删除图标
    private boolean hasFocus;// 是否有焦点

    public GenerateEditText(Context context) {
        super(context);
        init();
    }

    public GenerateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        modifyCursor(context, attrs);
        init();
    }

    public GenerateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        modifyCursor(context, attrs);
        init();
    }

    private void modifyCursor(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GenerateEditText);
        int drawable = array.getResourceId(R.styleable.GenerateEditText_clearDrawable, 0);
        if (drawable != 0){
            try {
                @SuppressLint("SoonBlockedPrivateApi") Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
                field.setAccessible(true);
                field.set(this, drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        array.recycle();
    }

    private void init() {
        clearDrawable = getCompoundDrawables()[2];
        if (clearDrawable == null){
            return;
        }
        clearDrawable.setBounds(0, 0 ,clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());
        setClearVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            if (getCompoundDrawables()[2] != null){
                if (event.getX() > (getWidth() - getTotalPaddingRight())
                    && event.getX() < (getWidth() - getPaddingRight())){
                    if(hasFocus) {
                        setText("");
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        setTextFilter(this);
        if (hasFocus){
            setClearVisible(text.length() > 0);
        }
    }

    private void setTextFilter(EditText editText) {
//        InputFilter filter = (src, start, end, dest, dStart, dEnd)->{
//            if (src.equals(" ")){
//                return "";
//            }
//            return null;
//        };
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence.equals(" ")){
                    return "";
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        hasFocus = b;
        if (hasFocus){
            setClearVisible(getText().length() > 0);
        }
    }

    private void setClearVisible(boolean visible) {
        Drawable clear = visible ? clearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], clear, getCompoundDrawables()[3]);
    }
}

package com.danny.xui.expand;

import android.content.Context;
import android.os.Build;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;

/**
 * @description 可展开控件
 * @author danny
 * @date 2018/9/28.
 */
public class ExpandTextView extends AppCompatTextView {
    private final String TEXT_EXPAND = " 全文";
    private final String TEXT_CLOSE = " 收起";

    private SpannableString spanExpand = null;
    private SpannableString spanClose = null;

    private int width = 0;// 最大扩展宽度
    private int maxLine = 2;// 最多显示行数

    private String original;// 文本

    public ExpandTextView(Context context) {
        super(context);
        initCloseEnd(context);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCloseEnd(context);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCloseEnd(context);
    }

    private void initCloseEnd(Context context) {
        String content = TEXT_EXPAND;
        spanClose = new SpannableString(content);
        SpanBtn span = new SpanBtn(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandTextView.super.setMaxLines(500);
                setExpandText(original);
            }
        }, context);
        spanClose.setSpan(span, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private void initExpandEnd(Context context) {
        String content = TEXT_CLOSE;
        spanExpand = new SpannableString(content);
        SpanBtn span = new SpanBtn(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandTextView.super.setMaxLines(maxLine);
                setCloseText(original);
            }
        }, context);
        spanExpand.setSpan(span, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置闭合文本
     */
    public void setCloseText(String text) {
        if (spanClose == null) {
            initCloseEnd(getContext());
        }

        boolean appendShowAll = false;// true 不需要展开收起功能    false 需要展开收起功能
        original = text.toString();

        int maxLines = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {// api大于16，可从资源中获取
            maxLines = getMaxLines();
        }else {
            maxLines = this.maxLine;
        }
        String workText = new StringBuilder(original).toString();

        if (maxLines != -1) {
            Layout layout = createWorkingLayout(workText);
            if (layout.getLineCount() > maxLines) {
                workText = original.substring(0, layout.getLineEnd(maxLines - 1)).trim();
                String showText = original.substring(0, layout.getLineEnd(maxLines - 1)).trim() + "..." + spanClose;
                Layout layout2 = createWorkingLayout(showText);
                while (layout2.getLineCount() > maxLines) {
                    int lastSpace = workText.length() - 1;
                    if (lastSpace == -1) {
                        break;
                    }
                    workText = workText.substring(0, lastSpace);
                    layout2 = createWorkingLayout(workText + "..." + spanClose);
                }
                appendShowAll = true;
                workText = workText + "...";
            }

        }

        setText(workText);
        if (appendShowAll) {
            append(spanClose);
            setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    /**
     *
     * @param text
     * @return
     */
    private Layout createWorkingLayout(CharSequence text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return new StaticLayout(text, getPaint(), width - getPaddingLeft() - getPaddingRight()
                    , Layout.Alignment.ALIGN_NORMAL, getLineSpacingMultiplier(), getLineSpacingExtra(), false);
        }else {
            return new StaticLayout(text, getPaint(), width - getPaddingLeft() - getPaddingRight()
                    , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
    }

    /**
     * 设置展开文本
     */
    private void setExpandText(String text) {
        if (spanExpand == null) {
            initExpandEnd(getContext());
        }
        Layout layout1 = createWorkingLayout(text);
        Layout layout2 = createWorkingLayout(text + TEXT_CLOSE);
        if (layout2.getLineCount() > layout1.getLineCount()) {
            Layout layout = createWorkingLayout(text + "\n");
            String exchangeTxt = text + "\n";
            String newTxt = text + TEXT_CLOSE;
            while (layout.getLineCount() == createWorkingLayout(newTxt).getLineCount()) {
                exchangeTxt = exchangeTxt + " ";
                newTxt = exchangeTxt + TEXT_CLOSE;
            }
            try {
                setText(exchangeTxt.substring(0, exchangeTxt.length() - 3));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Layout layout = createWorkingLayout(text);
            String exchangeTxt = text;
            String newTxt = text + TEXT_CLOSE;
            while (layout.getLineCount() == createWorkingLayout(newTxt).getLineCount()) {
                exchangeTxt = exchangeTxt + " ";
                newTxt = exchangeTxt + TEXT_CLOSE;
            }
            try {
                setText(exchangeTxt.substring(0, exchangeTxt.length() - 3));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        append(spanExpand);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
        super.setMaxLines(maxLine);
    }
}

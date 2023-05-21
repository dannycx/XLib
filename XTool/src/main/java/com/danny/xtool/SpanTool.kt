/*
 * Copyright (c) 2023 x
 */

package com.danny.xtool

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat

/**
 * 字符串工具
 * SpannableString使用
 * SPAN_INCLUSIVE_EXCLUSIVE 包括开始下标不包括结束
 * SPAN_EXCLUSIVE_EXCLUSIVE 不包括开始下标不包括结束
 *
 * @author danny
 * @since 2023/5/21
 */
object SpanTool {
    /**
     * 设置指定位置字符前景色
     *
     * @param context 上下文
     * @param text 文本
     * @param key 指定字符
     * @param resId 颜色id
     * @return Span字符串
     */
    fun specialText(context: Context, text: String, key: String, resId: Int): CharSequence {
        val ssb = SpannableStringBuilder(text)
        val fcs = ForegroundColorSpan(ContextCompat.getColor(context, resId))
        val start = text.indexOf(key)
        ssb.setSpan(fcs, start, start + key.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return ssb
    }

    /**
     * 处理字符串中指定位置字符显示大小
     *
     * @param context 上下文
     * @param text 字符
     * @param start 起始位置
     * @param length 长度
     * @param size 字体大小
     * @return span字符串
     */
    fun handlePointChar(context: Context, text: String, start: Int, length: Int, size: Int): SpannableString {
        val ss = SpannableString(text)
        val ass = AbsoluteSizeSpan(size, false)
        ss.setSpan(ass, start, start + length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置指定位置字符前景色
     *
     * @param context 上下文
     * @param text 文本
     * @param key 指定字符
     * @param resId 颜色id
     * @return
     */
    fun backgroundColorSpan(context: Context, text: String, key: String, resId: Int): SpannableString {
        val ss = SpannableString(text)
        val bcs = BackgroundColorSpan(ContextCompat.getColor(context, resId))
        val start = text.indexOf(key)
        ss.setSpan(bcs, start, start + key.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }
}

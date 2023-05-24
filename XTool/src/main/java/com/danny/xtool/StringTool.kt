/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.text.InputFilter
import java.lang.StringBuilder
import java.util.regex.Pattern
import kotlin.experimental.and

/**
 * 字符串工具类
 */
object StringTool {
    /**
     * 判断一个字符串的首字符是否为字母
     *
     * @param s 字符
     * @return true-是
     */
    fun initialAlphabet(s: String): Boolean {
        val c = s[0]
        val i = c.toInt()
        return i in 65..90 || i in 97..122
    }

    fun initialAlphabet2(s: String): Boolean {
        val c = s[0]
        return c in 'a'..'z' || c in 'A'..'Z'
    }

    /**
     * 判断是否为汉字
     *
     * @param str 字符串
     * @return true-汉字
     */
    fun isChinese(str: String): Boolean {
        val chars = str.toCharArray()
        var isGB2312 = false
        for (i in chars.indices) {
            val bytes = ("" + chars[i]).toByteArray()
            if (bytes.size == 2) {
                val ints = IntArray(2)
                ints[0] = (bytes[0] and 0xff.toByte()).toInt()
                ints[1] = (bytes[1] and 0xff.toByte()).toInt()
                if (ints[0] in 0x81..0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
                    isGB2312 = true
                    break
                }
            }
        }
        return isGB2312
    }

    /**
     * EditView过滤器
     *      不允许输入汉字
     */
    val filter = InputFilter { source, start, end, _, _, _ ->
        for (i in (start until end)) {
            if (isChinese(source.elementAt(i))) {
                return@InputFilter ""
            }
        }
        return@InputFilter null
    }

    /**
     * 是否是汉字
     */
    private fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
    }

    /**
     * 是否为空
     */
    fun isEmpty(args: Array<Any>?): Boolean = args.isNullOrEmpty()

    /**
     * 是否为空或null字符
     */
    fun isEmpty(str: String?): Boolean = (str.isNullOrEmpty() || "null" == str)

    private val set = HashSet<Char>(64)

    private val CHAR_SET = charArrayOf(
            '`', '~', '!', '@', '#', '$', '%', '^',
            '&', '*', '(', ')', '-', '_', '+', '=',
            '{', '}', '[', ']', '|', '\\', ':', ';',
            '\"', '\'', '<', '>', ',', '.', '?', '/'
    )

    init {
        for (c in CHAR_SET) {
            set.add(c)
        }
    }

    /**
     * 字符脱敏：保护字符
     */
    fun	desensitization(str: String?): String {
        return str?.let {
            val sb = StringBuilder()
            val length = str.length
            for (i in (0 until length)) {
                var c = str[i]
                if (i % 2 != 0 && !set.contains(c)) {
                    c = '*';
                }
                sb.append(c)
            }
            sb.toString()
        } ?: ""
    }

    /**
     * 判断字符串是否仅为数字
     */
    fun isNumber(str: String): Boolean {
        var i = str.length
        while (--i >= 0) {
            if (!Character.isDigit(str[i])) {
                return false
            }
        }
        return true
    }

    /**
     * 判断字符串是否仅为数字：正则表达式
     */
    fun isNum(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        return pattern.matcher(str).matches()
    }

    /**
     * 判断字符串是否仅为数字：ASCII码
     */
    fun isNumAscii(str: String): Boolean {
        var i = str.length
        while (--i >= 0) {
            val ch = str[i].toInt()
            if (ch < 48 || ch > 57) {
                return false
            }
        }
        return true
    }
}

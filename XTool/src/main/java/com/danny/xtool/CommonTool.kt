package com.danny.xtool

import android.os.Build
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




/**
 *
 *
 * @author danny
 * @since 2023/11/6
 */
class CommonTool {
    /**
     * 获取系统当前语言
     *
     * @return 系统当前语言
     */
    fun language(): String? {
        return Locale.getDefault().language
    }

    /**
     * 获取系统当前国家
     *
     * @return 系统当前国家
     */
    fun country(): String? {
        return Locale.getDefault().country
    }


    /**
     * 获取设备cpu数量
     *
     * @return cpu数量
     */
    fun cpuCount(): Int {
        return Runtime.getRuntime().availableProcessors()
    }

    /**
     * 获取设备cpu类型
     *
     * @return
     */
    fun cpuName(): String? {
        if (Build.SUPPORTED_ABIS.contains("arm")) {
            return "arm"
        } else if (Build.SUPPORTED_ABIS.equals("")) {
        }
        return null
    }

    /**
     * 获取系统时间
     *
     * @return 系统时间
     */
    fun time(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取系统时间
     *
     * @return 系统时间    2018-09-05 21:50:30
     */
    fun formatTime(): String? {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(System.currentTimeMillis()))
    }

}

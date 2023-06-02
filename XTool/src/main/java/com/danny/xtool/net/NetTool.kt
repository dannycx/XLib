/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission

/**
 * 网络工具类
 *
 * @author x
 * @since 2023-06-01
 */
object NetTool {
    /**
     * 获取网络类型:WiFi, 4G, 3G, 2G, UNKNOWN, NO
     */
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    fun getNetType(context: Context): XNetType {
        var netType = XNetType.NETWORK_NO
        val info = getNetworkInfo(context)
        info?.apply {
            when (isAvailable) {
                true -> {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> netType = XNetType.NETWORK_WIFI
                        ConnectivityManager.TYPE_MOBILE -> {
                            when (subtype) {
                                TelephonyManager.NETWORK_TYPE_IWLAN,
                                TelephonyManager.NETWORK_TYPE_LTE ->
                                    netType = XNetType.NETWORK_4G

                                TelephonyManager.NETWORK_TYPE_HSDPA,
                                TelephonyManager.NETWORK_TYPE_HSPA,
                                TelephonyManager.NETWORK_TYPE_HSUPA,
                                TelephonyManager.NETWORK_TYPE_HSPAP,
                                TelephonyManager.NETWORK_TYPE_EVDO_A,
                                TelephonyManager.NETWORK_TYPE_EVDO_B,
                                TelephonyManager.NETWORK_TYPE_EVDO_0,
                                TelephonyManager.NETWORK_TYPE_UMTS,
                                TelephonyManager.NETWORK_TYPE_EHRPD,
                                TelephonyManager.NETWORK_TYPE_TD_SCDMA ->
                                    netType = XNetType.NETWORK_3G

                                TelephonyManager.NETWORK_TYPE_EDGE,
                                TelephonyManager.NETWORK_TYPE_GPRS,
                                TelephonyManager.NETWORK_TYPE_CDMA,
                                TelephonyManager.NETWORK_TYPE_1xRTT,
                                TelephonyManager.NETWORK_TYPE_IDEN,
                                TelephonyManager.NETWORK_TYPE_GSM ->
                                    netType = XNetType.NETWORK_2G

                                else -> {
                                    netType = when (subtypeName) {
                                        "TD-SCDMA", "WCDMA", "CDMA2000" -> XNetType.NETWORK_3G
                                        else -> XNetType.NETWORK_UNKNOWN
                                    }
                                }
                            }
                        }
                        else -> netType = XNetType.NETWORK_UNKNOWN
                    }
                }
                else -> {}
            }
        }
        return netType
    }

    /**
     * 网络类型
     */
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    fun getNetworkType(context: Context): Int {
        val info = getNetworkInfo(context)
        return if (info != null && info.isAvailable) {
            info.type
        } else -1
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    fun getNetworkInfo(context: Context): NetworkInfo? {
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return manager?.activeNetworkInfo
    }

    /**
     * 是否有网
     */
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    fun hasInternet(context: Context): Boolean {
        val networkInfo = getNetworkInfo(context)
        return !(networkInfo == null || !networkInfo.isAvailable || !networkInfo.isConnected)
    }

    /**
     * 是否为手机网络
     */
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    fun isMobile(context: Context): Boolean {
        val info = getNetworkInfo(context)
        info?.apply {
            when (type) {
                ConnectivityManager.TYPE_MOBILE -> return true
            }
        }
        return false
    }

    /**
     * 是否为WiFi网络
     */
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    fun isWiFi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        info?.apply {
            when (type) {
                ConnectivityManager.TYPE_WIFI -> return true
            }
        }
        return false
    }
}

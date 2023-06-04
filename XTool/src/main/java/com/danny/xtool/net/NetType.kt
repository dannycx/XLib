/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.net

/**
 * 网络类型
 *
 * @author x
 * @since 2023-06-02
 */
enum class NetType(private var des: String) {
    NETWORK_WIFI("WiFi"), NETWORK_5G("5G"), NETWORK_4G("4G"), NETWORK_3G("3G")
        , NETWORK_2G("2G"), NETWORK_UNKNOWN("Unknown"), NETWORK_NO("No network");

    override fun toString(): String {
        return des
    }
}

/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.http

import com.danny.xnet.api.ApiService

/**
 * 网络请求工具
 *
 * @author x
 * @since 2023-05-31
 */
object NetTool {
    val apiService = ServiceCreator.createService(ApiService::class.java)
}

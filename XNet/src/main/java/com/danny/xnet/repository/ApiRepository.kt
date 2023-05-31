/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.repository

import com.danny.xnet.api.ApiTool


/**
 * Api存储类
 *
 * @author x
 * @since 2023-05-31
 */
class ApiRepository(private val apiHelper: ApiTool) {
    suspend fun getTests() = apiHelper.getTests()
}

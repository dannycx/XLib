/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.api

/**
 * 挂起函数
 *
 * @author x
 * @since 2023-05-31
 */
class ApiTool(private val apiService: ApiService) {
    suspend fun getTests() = apiService.getTests(1)
}

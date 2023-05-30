/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.api

import com.danny.xnet.data.Tests
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * ApiService
 *
 * @author x
 * @since 2023-05-30
 */
interface ApiService  {
    @GET("{page}")
    suspend fun getTests(@Path("page") page: Int): Tests
}

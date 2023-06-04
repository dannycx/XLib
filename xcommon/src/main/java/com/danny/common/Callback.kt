/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.common

/**
 * 回调
 *
 * @author danny
 * @since 2020-10-31
 */
interface Callback<Result> {
    fun success(result: Result)

    fun failure(t: Throwable, s: String)
}

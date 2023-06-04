/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.common

/**
 * 响应基类
 *
 * @author x
 * @since 2023-06-04
 */
class BaseResponse<Result> {
    var code: Int? = 0
    var message: String? = null
    var result: Result? = null
    var errMsg: String? = null
}

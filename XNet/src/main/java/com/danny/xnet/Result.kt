/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet

/**
 * 将网络调用的状态传给UI层
 *
 * @author x
 * @since 2023-05-30
 */
class Result<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?) = Result(Status.SUCCESS, data, null)
        fun <T> error(msg: String?, data: T?) = Result(Status.ERROR, data, msg)
        fun <T> loading(data: T?) = Result(Status.LOADING, data, null)
    }
}

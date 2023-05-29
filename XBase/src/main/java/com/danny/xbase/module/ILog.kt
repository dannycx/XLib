/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.module

/**
 * 日志
 *
 * @author danny
 * @since 2023-05-29
 */
interface ILog: IModule {
    fun v(tag: String, message: String)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)

    fun v(tag: String, message: String, e: Throwable)
    fun d(tag: String, message: String, e: Throwable)
    fun i(tag: String, message: String, e: Throwable)
    fun w(tag: String, message: String, e: Throwable)
    fun e(tag: String, message: String, e: Throwable)
}

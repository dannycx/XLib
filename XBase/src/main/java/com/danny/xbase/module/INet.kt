/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.module

/**
 * 网络请求
 *
 * @author danny
 * @since 2023-05-29
 */
interface INet: IModule {
    fun build()

    fun create(baseUrl: String)

//    fun create(baseUrl: String)
}

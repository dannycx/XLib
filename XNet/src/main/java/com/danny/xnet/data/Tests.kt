/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.data

import com.google.gson.annotations.SerializedName

/**
 * 测试数据
 *
 * @author x
 * @since 2023-05-30
 */
data class Tests(
    @SerializedName("data")
    val data: List<Test>,
    val page: Int,
    val page_count: Int,
    val status: Int,
    val total_counts: Int
)

data class Test(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("likeCounts")
    val likeCounts: Int,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("stars")
    val stars: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("views")
    val views: Int
)

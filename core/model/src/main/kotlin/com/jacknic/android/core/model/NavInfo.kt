package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 * 导航信息
 * @param articles 文章列表
 * @param cid 分类ID
 * @param name 分类名称
 */

data class NavInfo(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("cid")
    val cid: Int,
    @SerializedName("name")
    val name: String
)
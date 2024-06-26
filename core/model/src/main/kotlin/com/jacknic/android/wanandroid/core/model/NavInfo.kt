package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 导航信息
 * @param articles 文章列表
 * @param cid 分类ID
 * @param name 分类名称
 */

data class NavInfo(
    @SerializedName("articles")
    val articles: List<Article> = emptyList(),
    @SerializedName("cid")
    val cid: Int = 0,
    @SerializedName("name")
    val name: String = ""
)
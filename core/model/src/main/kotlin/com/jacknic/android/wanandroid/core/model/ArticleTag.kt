package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 文章标签
 *
 * @param name 标签名称
 * @param url 标签链接
 */
data class ArticleTag(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("url")
    val url: String = "",
)

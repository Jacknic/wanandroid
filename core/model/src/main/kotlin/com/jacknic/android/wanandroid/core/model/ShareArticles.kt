package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 分享文章信息
 * @param coinInfo
 * @param shareArticles
 */

data class ShareArticles(
    @SerializedName("coinInfo")
    val coinInfo: CoinInfo,
    @SerializedName("shareArticles")
    val shareArticles: List<Article> = emptyList()
)
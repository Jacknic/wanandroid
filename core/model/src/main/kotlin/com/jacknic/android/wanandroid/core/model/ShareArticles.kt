package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 分享文章信息
 * @param coinInfo 积分信息
 * @param shareArticles 分享文章分页数据
 */

data class ShareArticles(
    @SerializedName("coinInfo")
    val coinInfo: CoinInfo,
    @SerializedName("shareArticles")
    val shareArticles: Paging<Article>,
)

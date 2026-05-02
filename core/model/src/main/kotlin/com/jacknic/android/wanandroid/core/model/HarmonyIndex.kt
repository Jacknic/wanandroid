package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 鸿蒙专栏数据
 *
 * @param links 常用链接
 * @param openSources 开源项目
 * @param tools 常用工具
 */
data class HarmonyIndex(
    @SerializedName("links")
    val links: List<Chapter> = emptyList(),
    @SerializedName("open_sources")
    val openSources: List<Chapter> = emptyList(),
    @SerializedName("tools")
    val tools: List<Chapter> = emptyList()
)

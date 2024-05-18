package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 搜索热词
 * @param id 热词ID
 * @param name 热词值
 * @param order 排序
 * @param visible 可见标志
 * @param link
 */

data class HotKeyword (
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("order")
    val order: Int = 0,
    @SerializedName("visible")
    val visible: Int = 0,
    @SerializedName("link")
    val link: String? = null
)
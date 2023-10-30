package com.jacknic.android.core.model

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
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("visible")
    val visible: Int,
    @SerializedName("link")
    val link: String? = null
)
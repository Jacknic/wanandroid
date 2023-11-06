package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 * 常用网站
 * @param category 分类
 * @param icon 图标
 * @param id 友链ID
 * @param link 链接
 * @param name 名称
 * @param order 排序
 * @param visible 可见性
 */

data class FriendLink(
    @SerializedName("category")
    val category: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("link")
    val link: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("order")
    val order: Int = 0,
    @SerializedName("visible")
    val visible: Int = 0
)
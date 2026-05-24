package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 收藏的网址
 *
 * @param desc 描述
 * @param icon 图标
 * @param id 收藏ID
 * @param link 链接
 * @param name 名称
 * @param order 排序
 * @param userId 用户ID
 * @param visible 可见性，1为可见
 */
data class CollectTool(
    @SerializedName("desc")
    val desc: String = "",
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
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("visible")
    val visible: Int = 0,
)

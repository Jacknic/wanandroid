package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 * 首页banner
 *
 * @param desc 描述信息
 * @param id ID
 * @param imagePath 图片路径
 * @param isVisible 是否可见
 * @param order 排序
 * @param title 标题
 * @param type 类型
 * @param url 链接URL
 *
 * @author Jacknic
 */
data class Banner(
    @SerializedName("desc")
    val desc: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imagePath")
    val imagePath: String,
    @SerializedName("isVisible")
    val isVisible: Int,
    @SerializedName("order")
    val order: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("url")
    val url: String
)
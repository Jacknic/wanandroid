package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 站内消息
 *
 * @param category 分类
 * @param date 日期
 * @param fromUser 发送者
 * @param fullLink 完整链接
 * @param id 消息ID
 * @param link 链接
 * @param message 消息内容
 * @param niceDate 友好时间
 * @param publishDate 发布时间(时间戳)
 * @param title 标题
 * @param userId 用户ID
 */
data class Message(
    @SerializedName("category")
    val category: Int = 0,
    @SerializedName("date")
    val date: String = "",
    @SerializedName("fromUser")
    val fromUser: UserInfo = UserInfo(),
    @SerializedName("fullLink")
    val fullLink: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("link")
    val link: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("niceDate")
    val niceDate: String = "",
    @SerializedName("publishDate")
    val publishDate: Long = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("userId")
    val userId: Int = 0
)

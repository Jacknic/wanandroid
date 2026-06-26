package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 站内消息
 *
 * 注意：实际接口返回的 `date` 为时间戳（Long），`fromUser` 为发送者名称字符串。
 * OpenAPI 文档定义与实际不符，此处以实际返回为准。
 *
 * @param category 分类
 * @param date 日期时间戳
 * @param fromUser 发送者名称
 * @param fromUserId 发送者用户ID
 * @param fullLink 完整链接
 * @param id 消息ID
 * @param isRead 是否已读（1=已读，0=未读）
 * @param link 链接
 * @param message 消息内容
 * @param niceDate 友好时间
 * @param tag 消息标签（如"系统消息"）
 * @param title 标题
 * @param userId 接收者用户ID
 */
data class Message(
    @SerializedName("category")
    val category: Int = 0,
    @SerializedName("date")
    val date: Long = 0,
    @SerializedName("fromUser")
    val fromUser: String = "",
    @SerializedName("fromUserId")
    val fromUserId: Int = 0,
    @SerializedName("fullLink")
    val fullLink: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("isRead")
    val isRead: Int = 0,
    @SerializedName("link")
    val link: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("niceDate")
    val niceDate: String = "",
    @SerializedName("tag")
    val tag: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("userId")
    val userId: Int = 0,
)

package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 * 问答评论
 *
 * @param anonymous
 * @param appendForContent
 * @param articleId
 * @param canEdit
 * @param content
 * @param contentMd
 * @param id
 * @param niceDate
 * @param publishDate
 * @param replyCommentId
 * @param replyComments
 * @param rootCommentId
 * @param status
 * @param toUserId
 * @param toUserName
 * @param userId
 * @param userName
 * @param zan
 */
data class WendaComment(
    @SerializedName("anonymous")
    val anonymous: Int = 0,
    @SerializedName("appendForContent")
    val appendForContent: Int = 0,
    @SerializedName("articleId")
    val articleId: Int = 0,
    @SerializedName("canEdit")
    val canEdit: Boolean = false,
    @SerializedName("content")
    val content: String = "",
    @SerializedName("contentMd")
    val contentMd: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("niceDate")
    val niceDate: String = "",
    @SerializedName("publishDate")
    val publishDate: Int = 0,
    @SerializedName("replyCommentId")
    val replyCommentId: Int = 0,
    @SerializedName("replyComments")
    val replyComments: List<WendaComment> = emptyList(),
    @SerializedName("rootCommentId")
    val rootCommentId: Int = 0,
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("toUserId")
    val toUserId: Int = 0,
    @SerializedName("toUserName")
    val toUserName: String = "",
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("zan")
    val zan: Int = 0
)
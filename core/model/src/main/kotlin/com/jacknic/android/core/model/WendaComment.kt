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
data class WendaComment (
    @SerializedName("anonymous")
    val anonymous: Int,
    @SerializedName("appendForContent")
    val appendForContent: Int,
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("canEdit")
    val canEdit: Boolean,
    @SerializedName("content")
    val content: String,
    @SerializedName("contentMd")
    val contentMd: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("niceDate")
    val niceDate: String,
    @SerializedName("publishDate")
    val publishDate: Int,
    @SerializedName("replyCommentId")
    val replyCommentId: Int,
    @SerializedName("replyComments")
    val replyComments: List<WendaComment>,
    @SerializedName("rootCommentId")
    val rootCommentId: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("toUserId")
    val toUserId: Int,
    @SerializedName("toUserName")
    val toUserName: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("zan")
    val zan: Int
)
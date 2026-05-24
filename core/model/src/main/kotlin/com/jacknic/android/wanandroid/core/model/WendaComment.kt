package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 问答评论
 *
 * @param anonymous 是否匿名，0为非匿名
 * @param appendForContent 追加评论标志
 * @param articleId 文章ID
 * @param canEdit 是否可编辑
 * @param content 评论内容
 * @param contentMd 评论内容(Markdown格式)
 * @param id 评论ID，后续回复、删除都依赖此ID
 * @param niceDate 友好时间
 * @param publishDate 发布时间(时间戳)
 * @param replyCommentId 回复的评论ID
 * @param replyComments 回复评论列表
 * @param rootCommentId 根评论ID
 * @param status 状态
 * @param toUserId 被回复用户ID
 * @param toUserName 被回复用户名
 * @param userId 评论者用户ID
 * @param userName 评论者用户名
 * @param zan 点赞数
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
    val zan: Int = 0,
)

package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 阅读记录
 *
 * @param id 文章ID
 * @param title 标题
 * @param link 链接
 * @param author 作者
 * @param shareUser 分享人
 * @param niceDate 友好发布时间
 * @param envelopePic 封面图
 * @param desc 描述
 * @param chapterName 章节名称
 * @param superChapterName 父级分类名称
 * @param readTime 阅读时间戳(毫秒)
 */
data class ReadingHistory(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("link")
    val link: String = "",
    @SerializedName("author")
    val author: String = "",
    @SerializedName("shareUser")
    val shareUser: String = "",
    @SerializedName("niceDate")
    val niceDate: String = "",
    @SerializedName("envelopePic")
    val envelopePic: String = "",
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("chapterName")
    val chapterName: String = "",
    @SerializedName("superChapterName")
    val superChapterName: String = "",
    @SerializedName("readTime")
    val readTime: Long = System.currentTimeMillis(),
)

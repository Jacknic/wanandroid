package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 文章信息
 * @param adminAdd 管理员添加标志
 * @param apkLink
 * @param audit
 * @param author 作者
 * @param canEdit 是否可编辑
 * @param chapterId
 * @param chapterName
 * @param collect 是否已收藏
 * @param courseId 教程ID
 * @param desc 描述
 * @param descMd
 * @param envelopePic 封面图
 * @param fresh
 * @param host
 * @param id
 * @param isAdminAdd
 * @param link
 * @param niceDate
 * @param niceShareDate
 * @param origin
 * @param prefix
 * @param projectLink
 * @param publishTime
 * @param realSuperChapterId
 * @param selfVisible
 * @param shareDate
 * @param shareUser
 * @param superChapterId
 * @param superChapterName 父级分类ID
 * @param tags 附加标签信息
 * @param title 标题
 * @param type 类型
 * @param userId
 * @param visible
 * @param zan 点赞数
 *
 * @author Jacknic
 */
data class Article(
    @SerializedName("adminAdd")
    val adminAdd: Boolean = false,
    @SerializedName("apkLink")
    val apkLink: String = "",
    @SerializedName("audit")
    val audit: Int = 0,
    @SerializedName("author")
    val author: String = "",
    @SerializedName("canEdit")
    val canEdit: Boolean = false,
    @SerializedName("chapterId")
    val chapterId: Int = 0,
    @SerializedName("chapterName")
    val chapterName: String = "",
    @SerializedName("collect")
    val collect: Boolean = false,
    @SerializedName("courseId")
    val courseId: Int = 0,
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("descMd")
    val descMd: String = "",
    @SerializedName("envelopePic")
    val envelopePic: String = "",
    @SerializedName("fresh")
    val fresh: Boolean = false,
    @SerializedName("host")
    val host: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("isAdminAdd")
    val isAdminAdd: Boolean = false,
    @SerializedName("link")
    val link: String = "",
    @SerializedName("niceDate")
    val niceDate: String = "",
    @SerializedName("niceShareDate")
    val niceShareDate: String = "",
    @SerializedName("origin")
    val origin: String = "",
    @SerializedName("prefix")
    val prefix: String = "",
    @SerializedName("projectLink")
    val projectLink: String = "",
    @SerializedName("publishTime")
    val publishTime: Long = 0,
    @SerializedName("realSuperChapterId")
    val realSuperChapterId: Int = 0,
    @SerializedName("selfVisible")
    val selfVisible: Int = 0,
    @SerializedName("shareDate")
    val shareDate: Long = 0,
    @SerializedName("shareUser")
    val shareUser: String = "",
    @SerializedName("superChapterId")
    val superChapterId: Int = 0,
    @SerializedName("superChapterName")
    val superChapterName: String = "",
    @SerializedName("tags")
    val tags: List<Any> = emptyList(),
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("visible")
    val visible: Int = 0,
    @SerializedName("zan")
    val zan: Int = 0
)


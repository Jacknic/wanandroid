package com.jacknic.android.core.model


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
    val adminAdd: Boolean,
    @SerializedName("apkLink")
    val apkLink: String,
    @SerializedName("audit")
    val audit: Int,
    @SerializedName("author")
    val author: String,
    @SerializedName("canEdit")
    val canEdit: Boolean,
    @SerializedName("chapterId")
    val chapterId: Int,
    @SerializedName("chapterName")
    val chapterName: String,
    @SerializedName("collect")
    val collect: Boolean,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("descMd")
    val descMd: String,
    @SerializedName("envelopePic")
    val envelopePic: String,
    @SerializedName("fresh")
    val fresh: Boolean,
    @SerializedName("host")
    val host: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isAdminAdd")
    val isAdminAdd: Boolean,
    @SerializedName("link")
    val link: String,
    @SerializedName("niceDate")
    val niceDate: String,
    @SerializedName("niceShareDate")
    val niceShareDate: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("projectLink")
    val projectLink: String,
    @SerializedName("publishTime")
    val publishTime: Long,
    @SerializedName("realSuperChapterId")
    val realSuperChapterId: Int,
    @SerializedName("selfVisible")
    val selfVisible: Int,
    @SerializedName("shareDate")
    val shareDate: Long,
    @SerializedName("shareUser")
    val shareUser: String,
    @SerializedName("superChapterId")
    val superChapterId: Int,
    @SerializedName("superChapterName")
    val superChapterName: String,
    @SerializedName("tags")
    val tags: List<Any>,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("visible")
    val visible: Int,
    @SerializedName("zan")
    val zan: Int
)


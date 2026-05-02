package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 文章信息
 *
 * @param adminAdd 管理员添加标志
 * @param apkLink APK下载链接
 * @param audit 审核状态
 * @param author 作者，分享人分享的文章则为空字符串
 * @param canEdit 是否可编辑
 * @param chapterId 章节ID
 * @param chapterName 章节名称
 * @param collect 是否已收藏
 * @param courseId 教程ID
 * @param desc 描述
 * @param descMd 描述(Markdown格式)
 * @param envelopePic 封面图
 * @param fresh 是否为新发布
 * @param host 来源Host
 * @param id 文章ID
 * @param isAdminAdd 是否为管理员添加
 * @param link 文章链接
 * @param niceDate 友好发布时间，如"2天前"
 * @param niceShareDate 友好分享时间
 * @param origin 来源
 * @param prefix 前缀
 * @param projectLink 项目链接
 * @param publishTime 发布时间(时间戳毫秒)
 * @param realSuperChapterId 真实父级分类ID
 * @param selfVisible 自身可见性
 * @param shareDate 分享时间(时间戳毫秒)，可能为null
 * @param shareUser 分享人，非分享文章则为空字符串
 * @param superChapterId 父级分类ID，注意此id实际是一级分类的第一个子类目的id
 * @param superChapterName 父级分类名称
 * @param tags 附加标签信息
 * @param title 标题
 * @param type 类型，0为普通文章
 * @param userId 用户ID
 * @param visible 可见性，0为不可见，1为可见
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
    val shareDate: Long? = null,
    @SerializedName("shareUser")
    val shareUser: String = "",
    @SerializedName("superChapterId")
    val superChapterId: Int = 0,
    @SerializedName("superChapterName")
    val superChapterName: String = "",
    @SerializedName("tags")
    val tags: List<ArticleTag> = emptyList(),
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

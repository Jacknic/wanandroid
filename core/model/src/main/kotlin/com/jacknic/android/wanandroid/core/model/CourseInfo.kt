package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 教程信息
 *
 * @param author 作者
 * @param children 下属文章
 * @param courseId 教程ID
 * @param cover 封面图
 * @param desc 描述
 * @param id 教程ID，单个教程下所有文章列表会使用到
 * @param lisense 许可证
 * @param lisenseLink 许可证链接
 * @param name 教程名称
 * @param order 排序
 * @param parentChapterId 父级章节ID
 * @param userControlSetTop 是否置顶
 * @param visible 可见性
 *
 * @author Jacknic
 */
data class CourseInfo(
    @SerializedName("author")
    val author: String = "",
    @SerializedName("children")
    val children: List<String> = emptyList(),
    @SerializedName("courseId")
    val courseId: Int = 0,
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("lisense")
    val lisense: String = "",
    @SerializedName("lisenseLink")
    val lisenseLink: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("order")
    val order: Int = 0,
    @SerializedName("parentChapterId")
    val parentChapterId: Int = 0,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean = false,
    @SerializedName("visible")
    val visible: Int = 0
)

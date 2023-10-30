package com.jacknic.android.core.model

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
 * @param license
 * @param licenseLink
 * @param name 教程名称
 * @param order 排序
 * @param parentChapterId
 * @param userControlSetTop
 * @param visible
 *
 * @author Jacknic
 */
data class CourseInfo(
    @SerializedName("author")
    val author: String,
    @SerializedName("children")
    val children: List<String>,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("lisense")
    val license: String,
    @SerializedName("lisenseLink")
    val licenseLink: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("parentChapterId")
    val parentChapterId: Int,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean,
    @SerializedName("visible")
    val visible: Int
)
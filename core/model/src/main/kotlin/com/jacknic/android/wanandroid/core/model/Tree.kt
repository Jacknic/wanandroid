package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 体系数据，二级目录结构
 *
 * 一级目录包含children为二级分类列表。
 * JSON字段"lisense"/"lisenseLink"为API原始拼写（license的拼写错误）。
 *
 * @param articleList 文章列表
 * @param author 作者
 * @param children 二级目录
 * @param courseId 课程ID
 * @param cover 封面图
 * @param desc 描述
 * @param id 分类ID，查看该目录下所有文章时有用
 * @param lisense 许可证（API原始拼写）
 * @param lisenseLink 许可证链接（API原始拼写）
 * @param name 一级分类名称
 * @param order 排序
 * @param parentChapterId 父级分类ID，0表示顶级
 * @param type 类型
 * @param userControlSetTop 用户是否可置顶
 * @param visible 可见性，0为不可见，1为可见
 *
 * @author Jacknic
 */
data class Tree(
    @SerializedName("articleList")
    val articleList: List<Article> = emptyList(),
    @SerializedName("author")
    val author: String = "",
    @SerializedName("children")
    val children: List<Chapter> = emptyList(),
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
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean = false,
    @SerializedName("visible")
    val visible: Int = 0
)

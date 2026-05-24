package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 分类/章节，通用分类结构
 *
 * 用于项目分类、公众号列表、最受欢迎、工具列表等场景。
 * JSON字段"lisense"/"lisenseLink"为API原始拼写（license的拼写错误）。
 *
 * @param articleList 文章列表
 * @param author 作者
 * @param children 子分类
 * @param courseId 课程ID
 * @param cover 封面图
 * @param desc 描述
 * @param id 分类ID，查看该分类下文章时需要用到
 * @param lisense 许可证（API原始拼写）
 * @param lisenseLink 许可证链接（API原始拼写）
 * @param name 分类名称
 * @param order 排序
 * @param parentChapterId 父级分类ID，0表示顶级分类
 * @param type 类型
 * @param userControlSetTop 用户是否可置顶
 * @param visible 可见性，0为不可见，1为可见
 *
 * @author Jacknic
 */
data class Chapter(
    @SerializedName("articleList")
    val articleList: List<Article> = emptyList(),
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
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean = false,
    @SerializedName("visible")
    val visible: Int = 0,
)

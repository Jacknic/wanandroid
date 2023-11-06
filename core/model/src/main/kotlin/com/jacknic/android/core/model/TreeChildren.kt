package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 *
 * @param articleList
 * @param author
 * @param children
 * @param courseId
 * @param cover
 * @param desc
 * @param id
 * @param lisense
 * @param lisenseLink
 * @param name
 * @param order
 * @param parentChapterId
 * @param type
 * @param userControlSetTop
 * @param visible
 */

data class TreeChildren(
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
    val visible: Int = 0
)
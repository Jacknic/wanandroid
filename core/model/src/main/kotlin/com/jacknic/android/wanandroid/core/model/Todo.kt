package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * TODO信息
 *
 * @param id TODO唯一标识ID
 * @param date 预定完成日期，格式为yyyy-MM-dd
 * @param dateStr 日期字符串
 * @param title TODO标题
 * @param content TODO详情
 * @param status 完成状态，0为未完成，1为完成
 * @param type 类型，大于0的整数用于分类筛选，0为未设置
 * @param priority 优先级，大于0的整数
 * @param userId 用户ID
 * @param doneDate 完成日期
 * @param completeDate 完成时间，可能为null
 * @param visible 可见性，0为不可见，1为可见
 *
 * @author Jacknic
 */
data class Todo(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("date")
    val date: String = "",
    @SerializedName("dateStr")
    val dateStr: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("content")
    val content: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("priority")
    val priority: Int = 0,
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("doneDate")
    val doneDate: String = "",
    @SerializedName("completeDate")
    val completeDate: String? = null,
    @SerializedName("visible")
    val visible: Int = 0
)

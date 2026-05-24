package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 统一分页结构
 *
 * @param T 分页数据类型
 * @param curPage 当前页码
 * @param datas 数据列表
 * @param offset 数据偏移量
 * @param over 是否已越界（无更多数据）
 * @param pageCount 总页数
 * @param size 每页数据条数
 * @param total 数据总条数
 *
 * @author Jacknic
 */
data class Paging<T>(
    @SerializedName("curPage")
    val curPage: Int = 0,
    @SerializedName("datas")
    val datas: List<T> = emptyList(),
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("over")
    val over: Boolean = false,
    @SerializedName("pageCount")
    val pageCount: Int = 0,
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("total")
    val total: Int = 0,
)

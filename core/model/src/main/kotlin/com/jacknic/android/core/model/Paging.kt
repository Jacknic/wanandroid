package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 * 统一分页结构
 *
 * @author Jacknic
 */
data class Paging<T>(
    /**
     * 当前页
     */
    @SerializedName("curPage")
    val curPage: Int,

    /**
     * 数据列表
     */
    @SerializedName("datas")
    val datas: List<T>,

    /**
     * 数据偏移量
     */
    @SerializedName("offset")
    val offset: Int,

    /**
     * 是否已越界
     */
    @SerializedName("over")
    val over: Boolean,

    /**
     * 分页总数
     */
    @SerializedName("pageCount")
    val pageCount: Int,

    /**
     * 分页大小
     */
    @SerializedName("size")
    val size: Int,

    /**
     * 数据总数
     */
    @SerializedName("total")
    val total: Int
)
package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 积分明细记录
 *
 * @param coinCount 积分变动值
 * @param date 日期，如"2023-10-29 08:34"
 * @param desc 描述，积分获取原因
 * @param id 记录ID
 * @param level 等级
 * @param reason 原因
 * @param type 类型，1为加分，-1为扣分
 * @param userId 用户ID
 * @param username 用户名
 */
data class CoinDetail(
    @SerializedName("coinCount")
    val coinCount: Int = 0,
    @SerializedName("date")
    val date: String = "",
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("level")
    val level: Int = 0,
    @SerializedName("reason")
    val reason: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("username")
    val username: String = ""
)

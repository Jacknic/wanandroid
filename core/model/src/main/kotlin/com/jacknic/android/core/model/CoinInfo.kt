package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 * 个人积分信息
 * @param coinCount 总积分，总积分
 * @param level 等级
 * @param rank 当前排名，当前排名
 * @param userId 用户ID，用户ID
 * @param username 用户名，用户名
 */

data class CoinInfo(
    @SerializedName("coinCount")
    val coinCount: Int = 0,
    @SerializedName("level")
    val level: Int = 0,
    @SerializedName("rank")
    val rank: String = "",
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("username")
    val username: String = ""
)
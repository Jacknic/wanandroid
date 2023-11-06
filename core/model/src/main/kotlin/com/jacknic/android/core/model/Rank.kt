package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 * 积分排行
 * @param coinCount 总分
 * @param level 用户等级
 * @param nickname 昵称
 * @param rank 排名
 * @param userId 用户ID
 * @param username 用户名
 */

data class Rank(
    @SerializedName("coinCount")
    val coinCount: Int = 0,
    @SerializedName("level")
    val level: Int = 0,
    @SerializedName("nickname")
    val nickname: String = "",
    @SerializedName("rank")
    val rank: String = "",
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("username")
    val username: String = ""
)
package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 个人积分信息
 *
 * @param coinCount 总积分
 * @param level 等级
 * @param rank 当前排名，注意类型为String
 * @param userId 用户ID
 * @param username 用户名
 * @param nickname 昵称，可能为空字符串
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
    val username: String = "",
    @SerializedName("nickname")
    val nickname: String = "",
)

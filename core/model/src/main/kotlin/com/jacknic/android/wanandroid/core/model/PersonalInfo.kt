package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 用户个人信息，包含积分和用户信息
 *
 * @param coinInfo 积分信息
 * @param userInfo 用户信息
 */
data class PersonalInfo(
    @SerializedName("coinInfo")
    val coinInfo: CoinInfo = CoinInfo(),
    @SerializedName("userInfo")
    val userInfo: UserInfo = UserInfo(),
)

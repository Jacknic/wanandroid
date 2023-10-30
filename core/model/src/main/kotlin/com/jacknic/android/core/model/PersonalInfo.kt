package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 * 用户个人信息
 * @param coinInfo
 * @param userInfo
 */
data class PersonalInfo (
    @SerializedName("coinInfo")
    val coinInfo: CoinInfo,
    @SerializedName("userInfo")
    val userInfo: UserInfo
)
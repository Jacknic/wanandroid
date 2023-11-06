package com.jacknic.android.core.model

import com.google.gson.annotations.SerializedName

/**
 * 用户信息
 * @param admin
 * @param chapterTops
 * @param coinCount 可用
 * @param collectIds 可用
 * @param email 可用
 * @param icon
 * @param id 可用
 * @param nickname 可用
 * @param password
 * @param publicName
 * @param token
 * @param type
 * @param username 可用
 */

data class UserInfo(
    @SerializedName("admin")
    val admin: Boolean = false,
    @SerializedName("chapterTops")
    val chapterTops: List<String> = emptyList(),
    @SerializedName("coinCount")
    val coinCount: Int = 0,
    @SerializedName("collectIds")
    val collectIds: List<String> = emptyList(),
    @SerializedName("email")
    val email: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("nickname")
    val nickname: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("publicName")
    val publicName: String = "",
    @SerializedName("token")
    val token: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("username")
    val username: String
)
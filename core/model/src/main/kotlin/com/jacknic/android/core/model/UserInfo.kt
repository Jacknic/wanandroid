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

data class UserInfo (
    @SerializedName("admin")
    val admin: Boolean,
    @SerializedName("chapterTops")
    val chapterTops: List<String>,
    @SerializedName("coinCount")
    val coinCount: Int,
    @SerializedName("collectIds")
    val collectIds: List<String>,
    @SerializedName("email")
    val email: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("publicName")
    val publicName: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("username")
    val username: String
)
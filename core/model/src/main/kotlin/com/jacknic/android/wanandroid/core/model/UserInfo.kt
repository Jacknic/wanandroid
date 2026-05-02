package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 用户信息
 *
 * @param admin 是否管理员
 * @param chapterTops 置顶章节
 * @param coinCount 可用积分
 * @param collectIds 已收藏文章ID列表
 * @param email 邮箱
 * @param icon 头像
 * @param id 用户ID
 * @param nickname 昵称
 * @param password 密码（脱敏）
 * @param publicName 公开名称
 * @param token 认证Token
 * @param type 用户类型
 * @param username 用户名
 */
data class UserInfo(
    @SerializedName("admin")
    val admin: Boolean = false,
    @SerializedName("chapterTops")
    val chapterTops: List<String> = emptyList(),
    @SerializedName("coinCount")
    val coinCount: Int = 0,
    @SerializedName("collectIds")
    val collectIds: List<Int> = emptyList(),
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
    val username: String = ""
)

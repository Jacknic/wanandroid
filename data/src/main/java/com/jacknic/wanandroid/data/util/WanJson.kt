package com.jacknic.wanandroid.data.util

/**
 * 玩 Android 响应数据结构
 */
data class WanJson<T>(
    val errorCode: Int = 0,
    val errorMsg: String = "",
    val data: T? = null
)
package com.jacknic.android.wanandroid.core.data

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    /**
     * 是否跳过登录
     */
    fun skipLoginFlow(): Flow<Boolean>

    /**
     * 设置跳过登录状态
     */
    suspend fun setSkipLogin(skip: Boolean)
}
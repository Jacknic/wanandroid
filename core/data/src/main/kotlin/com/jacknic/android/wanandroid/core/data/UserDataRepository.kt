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

    /**
     * 搜索历史
     */
    fun searchHistoryFlow(): Flow<Set<String>>

    /**
     * 添加搜索历史
     */
    suspend fun addSearchHistory(keyword: String)

    /**
     * 删除单条搜索历史
     */
    suspend fun removeSearchHistory(keyword: String)

    /**
     * 清空搜索历史
     */
    suspend fun clearSearchHistory()
}

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
    fun searchHistoryFlow(): Flow<List<String>>

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

    // === 主题设置 ===

    /**
     * 主题模式
     */
    fun themeModeFlow(): Flow<String>

    /**
     * 设置主题模式
     */
    suspend fun setThemeMode(mode: String)

    /**
     * 是否启用动态颜色
     */
    fun dynamicThemeColorFlow(): Flow<Boolean>

    /**
     * 设置动态颜色开关
     */
    suspend fun setDynamicThemeColor(enabled: Boolean)

    /**
     * 主题颜色方案
     */
    fun themeColorSchemeFlow(): Flow<String>

    /**
     * 设置主题颜色方案
     */
    suspend fun setThemeColorScheme(scheme: String)

    /**
     * 自定义主题颜色（ARGB 整数）
     */
    fun customColorPrimaryFlow(): Flow<Int>

    /**
     * 设置自定义主题颜色
     */
    suspend fun setCustomColorPrimary(argb: Int)
}

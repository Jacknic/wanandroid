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

    // === 安全凭据管理 ===

    /**
     * 保存用户凭据（加密存储）
     *
     * @param username 用户名
     * @param password 密码
     * @param rememberPassword 是否记住密码
     */
    suspend fun saveCredentials(username: String, password: String, rememberPassword: Boolean)

    /**
     * 获取已保存的用户名
     */
    fun getSavedUsername(): String?

    /**
     * 获取已保存的密码
     */
    fun getSavedPassword(): String?

    /**
     * 是否记住密码
     */
    fun isRememberPassword(): Boolean

    /**
     * 清除已保存的凭据
     */
    suspend fun clearCredentials()

    /**
     * 是否存在已保存的凭据
     */
    fun hasCredentials(): Boolean
}

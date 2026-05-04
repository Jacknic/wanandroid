package com.jacknic.android.wanandroid.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(@param:ApplicationContext private val context: Context) {

    /**
     * 偏好设置项存储对象
     *
     * [DataStore使用指南](https://developer.android.google.cn/topic/libraries/architecture/datastore?hl=zh-cn)
     */
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val settings by lazy { context.dataStore }

    companion object {
        /**
         * 跳过登录标志
         */
        private val KEY_SKIP_LOGIN = booleanPreferencesKey("skip_login")

        /**
         * 搜索历史
         */
        private val KEY_SEARCH_HISTORY_LIST = stringPreferencesKey("search_history_list")

        private const val HISTORY_SEPARATOR = "\n"

        /**
         * 主题模式（SYSTEM/LIGHT/DARK）
         */
        private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")

        /**
         * 是否启用动态颜色
         */
        private val KEY_DYNAMIC_THEME_COLOR = booleanPreferencesKey("dynamic_theme_color")

        /**
         * 主题颜色方案
         */
        private val KEY_THEME_COLOR_SCHEME = stringPreferencesKey("theme_color_scheme")

        /**
         * 自定义主题颜色（ARGB 整数）
         */
        private val KEY_CUSTOM_COLOR_PRIMARY = intPreferencesKey("custom_color_primary")
    }

    /**
     * 是否跳过登录
     */
    fun skipLoginFlow() = settings.data.map { preferences ->
        preferences[KEY_SKIP_LOGIN] ?: false
    }

    /**
     * 设置跳过登录状态
     */
    suspend fun setSkipLogin(skip: Boolean) {
        settings.edit { preferences ->
            preferences[KEY_SKIP_LOGIN] = skip
        }
    }

    /**
     * 搜索历史（按最近搜索时间排序）
     */
    fun searchHistoryFlow() = settings.data.map { preferences ->
        preferences[KEY_SEARCH_HISTORY_LIST]?.split(HISTORY_SEPARATOR)?.filter { it.isNotEmpty() }
            ?: emptyList()
    }

    /**
     * 添加搜索历史（已存在则移至最前）
     */
    suspend fun addSearchHistory(keyword: String) {
        val current = searchHistoryFlow().first()
        val next = (listOf(keyword) + current.filter { it != keyword }).take(20)
        settings.edit { preferences ->
            preferences[KEY_SEARCH_HISTORY_LIST] = next.joinToString(HISTORY_SEPARATOR)
        }
    }

    /**
     * 删除单条搜索历史
     */
    suspend fun removeSearchHistory(keyword: String) {
        val current = searchHistoryFlow().first()
        val next = current - keyword
        settings.edit { preferences ->
            preferences[KEY_SEARCH_HISTORY_LIST] = next.joinToString(HISTORY_SEPARATOR)
        }
    }

    /**
     * 清空搜索历史
     */
    suspend fun clearSearchHistory() {
        settings.edit { preferences ->
            preferences.remove(KEY_SEARCH_HISTORY_LIST)
        }
    }

    // === 主题设置 ===

    /**
     * 主题模式
     */
    fun themeModeFlow() = settings.data.map { preferences ->
        preferences[KEY_THEME_MODE] ?: "SYSTEM"
    }

    /**
     * 设置主题模式
     */
    suspend fun setThemeMode(mode: String) {
        settings.edit { preferences ->
            preferences[KEY_THEME_MODE] = mode
        }
    }

    /**
     * 是否启用动态颜色
     */
    fun dynamicThemeColorFlow() = settings.data.map { preferences ->
        preferences[KEY_DYNAMIC_THEME_COLOR] ?: false
    }

    /**
     * 设置动态颜色开关
     */
    suspend fun setDynamicThemeColor(enabled: Boolean) {
        settings.edit { preferences ->
            preferences[KEY_DYNAMIC_THEME_COLOR] = enabled
        }
    }

    /**
     * 主题颜色方案
     */
    fun themeColorSchemeFlow() = settings.data.map { preferences ->
        preferences[KEY_THEME_COLOR_SCHEME] ?: "DEFAULT"
    }

    /**
     * 设置主题颜色方案
     */
    suspend fun setThemeColorScheme(scheme: String) {
        settings.edit { preferences ->
            preferences[KEY_THEME_COLOR_SCHEME] = scheme
        }
    }

    /**
     * 自定义主题颜色（ARGB 整数）
     */
    fun customColorPrimaryFlow() = settings.data.map { preferences ->
        preferences[KEY_CUSTOM_COLOR_PRIMARY] ?: 0xFF4483F4.toInt()
    }

    /**
     * 设置自定义主题颜色
     */
    suspend fun setCustomColorPrimary(argb: Int) {
        settings.edit { preferences ->
            preferences[KEY_CUSTOM_COLOR_PRIMARY] = argb
        }
    }
}

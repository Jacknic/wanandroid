package com.jacknic.android.wanandroid.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
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
        private val KEY_SEARCH_HISTORY = stringSetPreferencesKey("search_history")
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
     * 搜索历史
     */
    fun searchHistoryFlow() = settings.data.map { preferences ->
        preferences[KEY_SEARCH_HISTORY] ?: emptySet()
    }

    /**
     * 添加搜索历史
     */
    suspend fun addSearchHistory(keyword: String) {
        val current = searchHistoryFlow().first()
        val next = (listOf(keyword) + current.filter { it != keyword }).take(20).toSet()
        settings.edit { preferences ->
            preferences[KEY_SEARCH_HISTORY] = next
        }
    }

    /**
     * 删除单条搜索历史
     */
    suspend fun removeSearchHistory(keyword: String) {
        val current = searchHistoryFlow().first()
        val next = current - keyword
        settings.edit { preferences ->
            preferences[KEY_SEARCH_HISTORY] = next
        }
    }

    /**
     * 清空搜索历史
     */
    suspend fun clearSearchHistory() {
        settings.edit { preferences ->
            preferences.remove(KEY_SEARCH_HISTORY)
        }
    }
}

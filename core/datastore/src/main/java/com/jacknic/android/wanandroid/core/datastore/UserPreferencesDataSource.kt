package com.jacknic.android.wanandroid.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
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
        settings.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[KEY_SKIP_LOGIN] = skip
            }
        }
    }
}
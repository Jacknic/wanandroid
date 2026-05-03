package com.jacknic.android.wanandroid.core.data

import com.jacknic.android.wanandroid.core.datastore.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class DefaultUserDataRepository @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : UserDataRepository {
    override fun skipLoginFlow(): Flow<Boolean> = dataSource.skipLoginFlow()
    override suspend fun setSkipLogin(skip: Boolean) = dataSource.setSkipLogin(skip)
    override fun searchHistoryFlow(): Flow<Set<String>> = dataSource.searchHistoryFlow()
    override suspend fun addSearchHistory(keyword: String) = dataSource.addSearchHistory(keyword)
    override suspend fun removeSearchHistory(keyword: String) = dataSource.removeSearchHistory(keyword)
    override suspend fun clearSearchHistory() = dataSource.clearSearchHistory()
    override fun themeModeFlow(): Flow<String> = dataSource.themeModeFlow()
    override suspend fun setThemeMode(mode: String) = dataSource.setThemeMode(mode)
    override fun dynamicThemeColorFlow(): Flow<Boolean> = dataSource.dynamicThemeColorFlow()
    override suspend fun setDynamicThemeColor(enabled: Boolean) = dataSource.setDynamicThemeColor(enabled)
    override fun themeColorSchemeFlow(): Flow<String> = dataSource.themeColorSchemeFlow()
    override suspend fun setThemeColorScheme(scheme: String) = dataSource.setThemeColorScheme(scheme)
    override fun customColorPrimaryFlow(): Flow<Int> = dataSource.customColorPrimaryFlow()
    override suspend fun setCustomColorPrimary(argb: Int) = dataSource.setCustomColorPrimary(argb)
}

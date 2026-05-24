package com.jacknic.android.wanandroid.core.data

import com.jacknic.android.wanandroid.core.database.ReadingHistoryDao
import com.jacknic.android.wanandroid.core.database.ReadingHistoryEntity
import com.jacknic.android.wanandroid.core.database.toEntity
import com.jacknic.android.wanandroid.core.database.toModel
import com.jacknic.android.wanandroid.core.datastore.SecureCredentialsDataSource
import com.jacknic.android.wanandroid.core.datastore.UserPreferencesDataSource
import com.jacknic.android.wanandroid.core.model.ReadingHistory
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultUserDataRepository @Inject constructor(
    private val dataSource: UserPreferencesDataSource,
    private val secureCredentialsDataSource: SecureCredentialsDataSource,
    private val readingHistoryDao: ReadingHistoryDao,
) : UserDataRepository {
    override fun skipLoginFlow(): Flow<Boolean> = dataSource.skipLoginFlow()
    override suspend fun setSkipLogin(skip: Boolean) = dataSource.setSkipLogin(skip)
    override fun searchHistoryFlow(): Flow<List<String>> = dataSource.searchHistoryFlow()
    override suspend fun addSearchHistory(keyword: String) = dataSource.addSearchHistory(keyword)
    override suspend fun removeSearchHistory(keyword: String) = dataSource.removeSearchHistory(keyword)
    override suspend fun clearSearchHistory() = dataSource.clearSearchHistory()
    override fun readingHistoryFlow(): Flow<List<ReadingHistory>> =
        readingHistoryDao.getAll().map { entities -> entities.map { it.toModel() } }
    override suspend fun addReadingHistory(history: ReadingHistory) =
        readingHistoryDao.insert(history.toEntity())
    override suspend fun removeReadingHistory(articleId: Int) =
        readingHistoryDao.deleteById(articleId)
    override suspend fun clearReadingHistory() =
        readingHistoryDao.deleteAll()
    override fun themeModeFlow(): Flow<String> = dataSource.themeModeFlow()
    override suspend fun setThemeMode(mode: String) = dataSource.setThemeMode(mode)
    override fun dynamicThemeColorFlow(): Flow<Boolean> = dataSource.dynamicThemeColorFlow()
    override suspend fun setDynamicThemeColor(enabled: Boolean) = dataSource.setDynamicThemeColor(enabled)
    override fun themeColorSchemeFlow(): Flow<String> = dataSource.themeColorSchemeFlow()
    override suspend fun setThemeColorScheme(scheme: String) = dataSource.setThemeColorScheme(scheme)
    override fun customColorPrimaryFlow(): Flow<Int> = dataSource.customColorPrimaryFlow()
    override suspend fun setCustomColorPrimary(argb: Int) = dataSource.setCustomColorPrimary(argb)

    // === 安全凭据管理 ===

    override suspend fun saveCredentials(username: String, password: String, rememberPassword: Boolean) =
        secureCredentialsDataSource.saveCredentials(username, password, rememberPassword)

    override fun getSavedUsername(): String? = secureCredentialsDataSource.getSavedUsername()
    override fun getSavedPassword(): String? = secureCredentialsDataSource.getSavedPassword()
    override fun isRememberPassword(): Boolean = secureCredentialsDataSource.isRememberPassword()
    override suspend fun clearCredentials() = secureCredentialsDataSource.clearCredentials()
    override fun hasCredentials(): Boolean = secureCredentialsDataSource.hasCredentials()
}

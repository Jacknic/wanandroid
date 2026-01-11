package com.jacknic.android.wanandroid.core.data

import com.jacknic.android.wanandroid.core.datastore.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class DefaultUserDataRepository @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : UserDataRepository {
    override fun skipLoginFlow(): Flow<Boolean> = dataSource.skipLoginFlow()
    override suspend fun setSkipLogin(skip: Boolean) = dataSource.setSkipLogin(skip)
}
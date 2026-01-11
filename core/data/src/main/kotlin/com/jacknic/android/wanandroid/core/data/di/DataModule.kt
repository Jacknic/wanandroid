package com.jacknic.android.wanandroid.core.data.di

import com.jacknic.android.wanandroid.core.data.DefaultUserDataRepository
import com.jacknic.android.wanandroid.core.data.DefaultWanRepository
import com.jacknic.android.wanandroid.core.data.UserDataRepository
import com.jacknic.android.wanandroid.core.domain.WanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据模块默认绑定配置
 *
 * @author Jacknic
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    internal abstract fun wanRepository(
        repo: DefaultWanRepository
    ): WanRepository

    @Singleton
    @Binds
    internal abstract fun userDataRepository(
        repo: DefaultUserDataRepository
    ): UserDataRepository
}
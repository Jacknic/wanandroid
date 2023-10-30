package com.jacknic.android.core.data.di

import com.jacknic.android.core.data.DefaultWanRepository
import com.jacknic.android.core.domain.data.WanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * WanRepository 模块默认绑定配置
 *
 * @author Jacknic
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class WanRepositoryModule {

    @Binds
    abstract fun bindWanRepository(
        repo: DefaultWanRepository
    ): WanRepository
}
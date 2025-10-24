package com.jacknic.android.wanandroid.core.data

import com.jacknic.android.wanandroid.core.domain.WanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * [WanRepository] 模块默认绑定配置
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
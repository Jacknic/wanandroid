package com.jacknic.wanandroid.di

import android.content.Context
import com.jacknic.wanandroid.data.util.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * 其他依赖
 *
 * @author Jacknic
 */
@InstallIn(ApplicationComponent::class)
@Module
object CommonModule {

    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences.getInstance(context)
    }

}
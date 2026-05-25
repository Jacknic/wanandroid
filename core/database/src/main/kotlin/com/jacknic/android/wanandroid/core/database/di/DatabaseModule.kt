package com.jacknic.android.wanandroid.core.database.di

import android.content.Context
import androidx.room.Room
import com.jacknic.android.wanandroid.core.database.AppDatabase
import com.jacknic.android.wanandroid.core.database.ReadingHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库模块依赖注入配置
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "wanandroid.db",
    ).build()

    @Provides
    fun provideReadingHistoryDao(database: AppDatabase): ReadingHistoryDao = database.readingHistoryDao()
}

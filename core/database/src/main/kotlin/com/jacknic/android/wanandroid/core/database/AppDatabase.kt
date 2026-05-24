package com.jacknic.android.wanandroid.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * 应用数据库
 */
@Database(
    entities = [ReadingHistoryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 阅读记录数据访问对象
     */
    abstract fun readingHistoryDao(): ReadingHistoryDao
}

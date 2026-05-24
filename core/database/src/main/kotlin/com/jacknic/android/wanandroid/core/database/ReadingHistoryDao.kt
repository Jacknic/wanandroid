package com.jacknic.android.wanandroid.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * 阅读记录数据访问对象
 */
@Dao
interface ReadingHistoryDao {

    /**
     * 获取所有阅读记录，按阅读时间降序排列
     */
    @Query("SELECT * FROM reading_history ORDER BY readTime DESC")
    fun getAll(): Flow<List<ReadingHistoryEntity>>

    /**
     * 插入或更新阅读记录（已存在则替换）
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ReadingHistoryEntity)

    /**
     * 删除指定文章的阅读记录
     */
    @Query("DELETE FROM reading_history WHERE id = :articleId")
    suspend fun deleteById(articleId: Int)

    /**
     * 清空所有阅读记录
     */
    @Query("DELETE FROM reading_history")
    suspend fun deleteAll()
}

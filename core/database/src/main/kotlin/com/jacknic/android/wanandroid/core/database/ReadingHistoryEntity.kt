package com.jacknic.android.wanandroid.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jacknic.android.wanandroid.core.model.ReadingHistory

/**
 * 阅读记录数据库实体
 */
@Entity(tableName = "reading_history")
data class ReadingHistoryEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val link: String,
    val author: String,
    val shareUser: String,
    val niceDate: String,
    val envelopePic: String,
    val desc: String,
    val chapterName: String,
    val superChapterName: String,
    val readTime: Long,
)

/**
 * 将 [ReadingHistory] 转换为 [ReadingHistoryEntity]
 */
fun ReadingHistory.toEntity() = ReadingHistoryEntity(
    id = id,
    title = title,
    link = link,
    author = author,
    shareUser = shareUser,
    niceDate = niceDate,
    envelopePic = envelopePic,
    desc = desc,
    chapterName = chapterName,
    superChapterName = superChapterName,
    readTime = readTime,
)

/**
 * 将 [ReadingHistoryEntity] 转换为 [ReadingHistory]
 */
fun ReadingHistoryEntity.toModel() = ReadingHistory(
    id = id,
    title = title,
    link = link,
    author = author,
    shareUser = shareUser,
    niceDate = niceDate,
    envelopePic = envelopePic,
    desc = desc,
    chapterName = chapterName,
    superChapterName = superChapterName,
    readTime = readTime,
)

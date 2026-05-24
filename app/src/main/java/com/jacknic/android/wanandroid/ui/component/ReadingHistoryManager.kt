package com.jacknic.android.wanandroid.ui.component

import com.jacknic.android.wanandroid.core.data.UserDataRepository
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.ReadingHistory
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 阅读记录管理器 - 全局单例，管理文章阅读记录
 */
@Singleton
class ReadingHistoryManager @Inject constructor(private val userDataRepo: UserDataRepository) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * 阅读记录列表
     */
    val readingHistoryFlow: Flow<List<ReadingHistory>> = userDataRepo.readingHistoryFlow()

    /**
     * 记录文章阅读（已存在则更新阅读时间至最前）
     */
    fun addReadingHistory(article: Article) {
        scope.launch {
            val history = ReadingHistory(
                id = article.id,
                title = article.title,
                link = article.link,
                author = article.author,
                shareUser = article.shareUser,
                niceDate = article.niceDate,
                envelopePic = article.envelopePic,
                desc = article.desc,
                chapterName = article.chapterName,
                superChapterName = article.superChapterName,
                readTime = System.currentTimeMillis(),
            )
            userDataRepo.addReadingHistory(history)
        }
    }

    /**
     * 删除单条阅读记录
     */
    suspend fun removeReadingHistory(articleId: Int) {
        userDataRepo.removeReadingHistory(articleId)
    }

    /**
     * 清空阅读记录
     */
    suspend fun clearReadingHistory() {
        userDataRepo.clearReadingHistory()
    }

    /**
     * 获取当前阅读记录数量
     */
    suspend fun getReadingHistoryCount(): Int = userDataRepo.readingHistoryFlow().first().size
}

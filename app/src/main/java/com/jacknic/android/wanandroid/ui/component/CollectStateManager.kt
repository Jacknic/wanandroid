package com.jacknic.android.wanandroid.ui.component

import com.jacknic.android.wanandroid.core.domain.WanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 收藏状态管理器 - 全局单例，管理文章收藏状态
 *
 * 负责维护收藏ID集合、提供收藏/取消收藏操作、云端同步。
 * 登录时从 UserInfo.collectIds 初始化，登出时清空。
 */
@Singleton
class CollectStateManager @Inject constructor(
    private val repo: WanRepository
) {
    private val _collectIds = MutableStateFlow<Set<Int>>(emptySet())
    val collectIds: StateFlow<Set<Int>> = _collectIds.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    /**
     * 从登录信息初始化收藏ID集合
     */
    fun initFromUserInfo(collectIds: List<Int>) {
        _collectIds.value = collectIds.toSet()
        _isInitialized.value = true
    }

    /**
     * 清空收藏状态（登出时调用）
     */
    fun clear() {
        _collectIds.value = emptySet()
        _isInitialized.value = false
    }

    /**
     * 切换收藏状态（文章列表页面使用，调用 uncollectOriginId 接口）
     *
     * @param articleId 文章原始ID
     * @param isCurrentlyCollected 当前是否已收藏
     * @return 操作结果
     */
    suspend fun toggleCollect(articleId: Int, isCurrentlyCollected: Boolean): Result<Any?> {
        return if (isCurrentlyCollected) {
            repo.uncollectOriginId(articleId).also { result ->
                if (result.isSuccess) {
                    _collectIds.update { it - articleId }
                }
            }
        } else {
            repo.collectArticle(articleId).also { result ->
                if (result.isSuccess) {
                    _collectIds.update { it + articleId }
                }
            }
        }
    }

    /**
     * 取消收藏（我的收藏页面使用，调用 uncollect 接口）
     *
     * @param collectId 收藏记录ID
     * @param articleId 文章原始ID（用于更新本地状态）
     * @return 操作结果
     */
    suspend fun uncollectFromCollection(collectId: Int, articleId: Int): Result<Any?> {
        return repo.uncollect(collectId).also { result ->
            if (result.isSuccess) {
                _collectIds.update { it - articleId }
            }
        }
    }

    /**
     * 判断文章是否已收藏
     */
    fun isCollected(articleId: Int): Boolean = _collectIds.value.contains(articleId)
}

package com.jacknic.android.wanandroid.ui.component

import com.jacknic.android.wanandroid.core.domain.WanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 收藏操作结果
 */
sealed class CollectResult {
    /** 操作成功 */
    data object Success : CollectResult()

    /** 未登录 */
    data object NotLoggedIn : CollectResult()

    /** 操作失败 */
    data class Error(val message: String) : CollectResult()
}

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
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _collectIds = MutableStateFlow<Set<Int>>(emptySet())
    val collectIds: StateFlow<Set<Int>> = _collectIds.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    /** 是否已登录 */
    val isLoggedIn: Boolean get() = _isInitialized.value

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
    suspend fun toggleCollect(articleId: Int, isCurrentlyCollected: Boolean): CollectResult {
        if (!isLoggedIn) return CollectResult.NotLoggedIn

        return if (isCurrentlyCollected) {
            val result = repo.uncollectOriginId(articleId)
            if (result.isSuccess) {
                _collectIds.update { it - articleId }
                syncCollectIds()
                CollectResult.Success
            } else {
                CollectResult.Error(result.exceptionOrNull()?.message ?: "取消收藏失败")
            }
        } else {
            val result = repo.collectArticle(articleId)
            if (result.isSuccess) {
                _collectIds.update { it + articleId }
                syncCollectIds()
                CollectResult.Success
            } else {
                CollectResult.Error(result.exceptionOrNull()?.message ?: "收藏失败")
            }
        }
    }

    /**
     * 取消收藏（我的收藏页面使用，调用 uncollect 接口）
     *
     * @param articleId 文章原始ID（用于更新本地状态）
     * @return 操作结果
     */
    suspend fun uncollectFromCollection(articleId: Int): CollectResult {
        if (!isLoggedIn) return CollectResult.NotLoggedIn

        val result = repo.uncollectOriginId(articleId)
        return if (result.isSuccess) {
            _collectIds.update { it - articleId }
            syncCollectIds()
            CollectResult.Success
        } else {
            CollectResult.Error(result.exceptionOrNull()?.message ?: "取消收藏失败")
        }
    }

    /**
     * 判断文章是否已收藏
     */
    fun isCollected(articleId: Int): Boolean = _collectIds.value.contains(articleId)

    /**
     * 从服务器同步收藏ID集合（静默同步，失败不影响本地状态）
     */
    private fun syncCollectIds() {
        scope.launch {
            try {
                val result = repo.getUserLgUserinfo()
                result.onSuccess { personalInfo ->
                    _collectIds.value = personalInfo.userInfo.collectIds.toSet()
                }
            } catch (_: Exception) {
                // 静默失败，不影响本地状态
            }
        }
    }
}

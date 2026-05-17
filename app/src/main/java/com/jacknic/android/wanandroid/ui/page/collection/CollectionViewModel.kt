package com.jacknic.android.wanandroid.ui.page.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.common.withLoading
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Paging
import com.jacknic.android.wanandroid.ui.component.CollectStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val repo: WanRepository,
    private val collectStateManager: CollectStateManager
) : ViewModel() {

    private val _collectList = MutableStateFlow<StateResult<Paging<Article>>?>(null)
    val collectList = _collectList.asStateFlow()

    private var currentPage = 0
    private var hasMore = true

    init {
        loadCollectList()
    }

    private fun loadCollectList() {
        viewModelScope.launch {
            _collectList.withLoading {
                repo.getLgCollectList(0).toStateResult()
            }
            val paging = (_collectList.value as? StateResult.Success)?.data
            currentPage = paging?.curPage ?: 0
            hasMore = !(paging?.over ?: true)
        }
    }

    fun loadMore() {
        if (!hasMore) return
        val currentData = (_collectList.value as? StateResult.Success)?.data ?: return
        viewModelScope.launch {
            try {
                val result = repo.getLgCollectList(currentPage)
                result.onSuccess { paging ->
                    _collectList.update { prevState ->
                        val prevPaging = (prevState as? StateResult.Success)?.data
                        if (prevPaging != null) {
                            StateResult.Success(
                                prevPaging.copy(
                                    datas = prevPaging.datas + paging.datas,
                                    curPage = paging.curPage,
                                    over = paging.over
                                )
                            )
                        } else {
                            StateResult.Success(paging)
                        }
                    }
                    currentPage = paging.curPage
                    hasMore = !paging.over
                }
            } catch (_: Exception) {
                // 加载更多失败，保持现有数据
            }
        }
    }

    /**
     * 取消收藏（我的收藏页面）
     * 使用 uncollect 接口（传入收藏记录ID），同时更新全局收藏状态
     */
    fun uncollect(article: Article) {
        viewModelScope.launch {
            try {
                collectStateManager.uncollectFromCollection(article.id, article.id)
                _collectList.update { prevState ->
                    val prevPaging = (prevState as? StateResult.Success)?.data ?: return@update prevState
                    StateResult.Success(
                        prevPaging.copy(
                            datas = prevPaging.datas.filter { it.id != article.id },
                            total = (prevPaging.total - 1).coerceAtLeast(0)
                        )
                    )
                }
            } catch (_: Exception) {
                // 取消收藏失败
            }
        }
    }

    fun refresh() {
        currentPage = 0
        hasMore = true
        loadCollectList()
    }
}

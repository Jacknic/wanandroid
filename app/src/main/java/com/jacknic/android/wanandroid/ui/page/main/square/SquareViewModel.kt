package com.jacknic.android.wanandroid.ui.page.main.square

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.util.PagingListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 广场页面视图数据
 */
@HiltViewModel
class SquareViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repo: WanRepository
) : ViewModel() {

    private var articleListFlow: Flow<PagingData<Article>>? = null

    /**
     * 获取广场文章分页流
     */
    fun getArticleListFlow(): Flow<PagingData<Article>> {
        return articleListFlow ?: PagingListDataSource.pager(
            loadAction = { page, pageSize -> repo.getUserArticleList(page, pageSize) }
        ).flow.cachedIn(viewModelScope).also { articleListFlow = it }
    }

    // ==================== 状态保存与恢复 ====================

    fun saveScrollState(firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) {
        savedStateHandle[KEY_SCROLL_INDEX] = firstVisibleItemIndex
        savedStateHandle[KEY_SCROLL_OFFSET] = firstVisibleItemScrollOffset
    }

    fun getScrollState(): Pair<Int, Int> {
        val index = savedStateHandle[KEY_SCROLL_INDEX] ?: 0
        val offset = savedStateHandle[KEY_SCROLL_OFFSET] ?: 0
        return index to offset
    }

    companion object {
        private const val KEY_SCROLL_INDEX = "square_scroll_index"
        private const val KEY_SCROLL_OFFSET = "square_scroll_offset"
    }
}

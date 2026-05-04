package com.jacknic.android.wanandroid.ui.page.main.square

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
}

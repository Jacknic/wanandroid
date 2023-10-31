package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jacknic.android.core.domain.data.WanRepository
import com.jacknic.android.core.model.Article

/**
 * 首页文章数据源
 *
 * @author Jacknic
 */
class HomeArticleListDataSource constructor(
    private val repo: WanRepository
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        val result = repo.getHomeArticleList(page, pageSize)
        return result.fold({
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (it.datas.run { isEmpty() }) null else page + 1
            LoadResult.Page(it.datas, prevKey, nextKey)
        }, {
            LoadResult.Error(it)
        })
    }
}
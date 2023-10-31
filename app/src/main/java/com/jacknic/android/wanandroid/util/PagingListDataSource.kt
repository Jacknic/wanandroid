package com.jacknic.android.wanandroid.util

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jacknic.android.core.model.Paging

/**
 * 通用分页数据源
 *
 * @author Jacknic
 */
class PagingListDataSource<T : Any> constructor(
    private val loadAction: suspend (page: Int, pageSize: Int) -> Result<Paging<T>>,
    private val initPage: Int = 0
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: initPage
        val result = loadAction(page, params.loadSize)
        return result.fold({
            val prevKey = if (page <= initPage) null else page - 1
            val nextKey = if (it.datas.isEmpty()) null else page + 1
            LoadResult.Page(it.datas, prevKey, nextKey)
        }, {
            LoadResult.Error(it)
        })
    }

    companion object {
        /**
         * 创建分页数据对象
         *
         * @param loadAction 分页加载逻辑
         * @param pageSize 分页大小
         * @param initPage 初始加载页面
         */
        fun <T : Any> pager(
            loadAction: suspend (page: Int, pageSize: Int) -> Result<Paging<T>>,
            pageSize: Int = 30,
            initPage: Int = 0
        ): Pager<Int, T> {
            val pagingConfig = PagingConfig(pageSize, initialLoadSize = pageSize)
            return Pager(pagingConfig) {
                PagingListDataSource({ page, pageSize -> loadAction(page, pageSize) }, initPage)
            }
        }
    }
}
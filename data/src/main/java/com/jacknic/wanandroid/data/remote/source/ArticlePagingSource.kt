package com.jacknic.wanandroid.data.remote.source

import androidx.paging.PagingSource
import com.jacknic.wanandroid.data.model.Article
import com.jacknic.wanandroid.data.util.SafeResult
import com.jacknic.wanandroid.data.util.safeApiCall

/**
 * 文章分页数据源
 *
 * @author Jacknic
 */
class ArticlePagingSource(private val wanDataSource: WanDataSource) : PagingSource<Int, Article>() {

    private val startIndex = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageNo = params.key ?: startIndex
        return when (val result = safeApiCall { wanDataSource.listArticle(pageNo, null) }) {
            is SafeResult.Success -> {
                val pageList = result.data
                val dataList = pageList.datas
                LoadResult.Page(
                    data = dataList,
                    prevKey = if (pageNo == startIndex) null else pageNo - 1,
//                    itemsAfter = pageList.total - pageList.offset,
                    nextKey = if (dataList.isEmpty()) null else pageNo + 1
                )
            }
            is SafeResult.Error -> {
                LoadResult.Error(result.exception)
            }
        }
    }
}
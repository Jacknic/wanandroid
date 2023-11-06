package com.jacknic.android.core.data

import com.jacknic.android.core.domain.data.WanRepository
import com.jacknic.android.core.model.Article
import com.jacknic.android.core.model.Paging
import com.jacknic.android.core.network.WanApi
import com.jacknic.android.core.network.runResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 数据读取默认实现类
 *
 * @author Jacknic
 */
@Singleton
class DefaultWanRepository @Inject constructor(private val api: WanApi) : WanRepository {

    override suspend fun getHomeArticleList(
        page: Int,
        pageSize: Int
    ): Result<Paging<Article>> = runResult { api.getArticleList(page, pageSize) }

    override suspend fun getHomeBannerList() = runResult(api::getBannerList)
}
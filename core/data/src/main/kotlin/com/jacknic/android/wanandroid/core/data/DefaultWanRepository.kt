package com.jacknic.android.wanandroid.core.data

import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Paging
import com.jacknic.android.wanandroid.core.network.WanApi
import com.jacknic.android.wanandroid.core.network.runResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 数据读取默认实现类
 *
 * @author Jacknic
 */
@Singleton
internal class DefaultWanRepository @Inject constructor(private val api: WanApi) : WanRepository {

    override suspend fun getHomeArticleList(
        page: Int,
        pageSize: Int
    ): Result<Paging<Article>> = runResult { api.getArticleList(page, pageSize) }

    override suspend fun getHomeBannerList() = runResult(api::getBannerList)

    override suspend fun login(username: String, password: String) =
        runResult { api.postUserLogin(username, password) }
}
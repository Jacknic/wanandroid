package com.jacknic.wanandroid.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.jacknic.wanandroid.data.remote.source.ArticlePagingSource
import com.jacknic.wanandroid.data.remote.source.WanDataSource
import com.jacknic.wanandroid.data.util.safeApiCall

/**
 * 玩Android数据访问仓库
 *
 * @author Jacknic
 */
class WanRepository(private val source: WanDataSource) {

    suspend fun login(username: String, password: String) =
        safeApiCall { source.login(username, password) }

    suspend fun fetchBanner() = safeApiCall { source.listBanner() }

    suspend fun fetchHomeList(pageNo: Int) = safeApiCall { source.listArticle(pageNo) }

    fun fetchHomeList2() = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            ArticlePagingSource(source)
        }
    )

    suspend fun fetchTopList() = safeApiCall { source.getTopList() }

    suspend fun logout() = safeApiCall { source.logout() }

    suspend fun fetchCollectData(pageNo: Int) = safeApiCall { source.listCollect(pageNo) }

    suspend fun fetchIntegral() = safeApiCall { source.getIntegral() }

    suspend fun collect(id: Int) = safeApiCall { source.collect(id) }

    suspend fun unCollect(id: Int) = safeApiCall { source.unCollect(id) }

    suspend fun fetchProjectTabList() = safeApiCall { source.fetchProjectTabList() }

    suspend fun fetchAccountTabList() = safeApiCall { source.listAccountTab() }

    suspend fun fetchProjectList(pageNo: Int, cid: Int) =
        safeApiCall { source.listProject(pageNo, cid) }

    suspend fun listWeChatArticle(cid: Int, pageNo: Int) =
        safeApiCall { source.listWeChatArticle(cid, pageNo) }

    suspend fun search(pageNo: Int, k: String) = safeApiCall { source.search(pageNo, k) }

    suspend fun fetchSystemList() = safeApiCall { source.getSystemList() }

    suspend fun fetchSystemArticle(pageNo: Int, cid: Int) =
        safeApiCall { source.listArticle(pageNo, cid) }

    suspend fun fetchNavigation() = safeApiCall { source.fetchNavigation() }

    suspend fun fetchRank(pageNo: Int) = safeApiCall { source.fetchRank(pageNo) }

    suspend fun fetchIntegralRecord(pageNo: Int) =
        safeApiCall { source.listIntegralRecord(pageNo) }

    suspend fun fetchMyArticle(pageNo: Int) = safeApiCall { source.listMyArticle(pageNo) }

    suspend fun deleteMyArticle(id: Int) = safeApiCall { source.deleteMyArticle(id) }

    suspend fun shareArticle(title: String, link: String) =
        safeApiCall { source.shareArticle(title, link) }

    suspend fun register(username: String, password: String, rePassword: String) =
        safeApiCall { source.register(username, password, rePassword) }
}
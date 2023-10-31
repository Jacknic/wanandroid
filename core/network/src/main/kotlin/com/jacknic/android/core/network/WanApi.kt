package com.jacknic.android.core.network

import com.jacknic.android.core.model.Article
import com.jacknic.android.core.model.Paging
import com.jacknic.android.core.model.WanResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 玩安卓服务接口定义
 *
 * @author Jacknic
 */
interface WanApi {

    /**
     * 获取首页文章列表
     *
     * @param page 页码
     * @param pageSize 分页大小
     */
    @PageNotice
    @GET("/article/list/{page}/json")
    suspend fun getArticleList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int
    ): WanResult<Paging<Article>>

}
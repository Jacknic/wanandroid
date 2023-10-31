package com.jacknic.android.core.domain.data

import com.jacknic.android.core.model.Article
import com.jacknic.android.core.model.Paging

/**
 * 玩安卓数据读取定义
 *
 * @author Jacknic
 */
interface WanRepository {

    /**
     * 获取首页文章列表
     *
     * @param page 页码
     * @param pageSize 分页大小
     */
    suspend fun getHomeArticleList(page: Int, pageSize: Int): Result<Paging<Article>>
}
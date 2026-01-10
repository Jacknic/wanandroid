package com.jacknic.android.wanandroid.core.domain

import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Banner
import com.jacknic.android.wanandroid.core.model.Paging
import com.jacknic.android.wanandroid.core.model.UserInfo

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

    /**
     *获取首页
     */
    suspend fun getHomeBannerList(): Result<List<Banner>>

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     */
    suspend fun login(username: String, password: String): Result<UserInfo>

}
package com.jacknic.wanandroid.data.remote.api

import com.jacknic.wanandroid.data.model.*
import com.jacknic.wanandroid.data.util.WanResponse
import retrofit2.http.*

/**
 * 玩Android 开放API
 *
 * https://wanandroid.com/blog/show/2
 */
interface WanApi {

    /**
     * 获取首页置顶文章数据
     */
    @GET("/article/top/json")
    suspend fun getTopList(): WanResponse<List<Article>>

    /**
     * banner
     */
    @GET("/banner/json")
    suspend fun listBanner(): WanResponse<List<Banner>>

    /**
     * 登录
     *
     * [username] 用户名
     * [password] 密码
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): WanResponse<User>

    @GET("/user/logout/json")
    suspend fun logout(): WanResponse<Any>

    /**
     * 获取收藏文章数据
     */
    @GET("/lg/collect/list/{pageNo}/json")
    suspend fun listCollect(
        @Path("pageNo") pageNo: Int
    ): WanResponse<Collect>

    /**
     * 获取个人积分
     */
    @GET("/lg/coin/userinfo/json")
    suspend fun getIntegral(): WanResponse<Integral>

    /**
     * 收藏
     */
    @POST("/lg/collect/{id}/json")
    suspend fun collect(
        @Path("id") id: Int
    ): WanResponse<Any>

    /**
     * 取消收藏
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun unCollect(
        @Path("id") id: Int
    ): WanResponse<Any>

    /**
     * 获取项目tab
     */
    @GET("/project/tree/json")
    suspend fun listProjectTab(): WanResponse<List<Tab>>

    /**
     * 获取项目tab
     */
    @GET("/wxarticle/chapters/json")
    suspend fun listAccountTab(): WanResponse<List<Tab>>

    /**
     * 获取项目列表
     *
     * [pageNo] 页码
     * [cid] 分类ID
     */
    @GET("/project/list/{pageNo}/json")
    suspend fun listProject(
        @Path("pageNo") pageNo: Int,
        @Query("cid") cid: Int
    ): WanResponse<Article>

    /**
     * 获取公众号列表
     */
    @GET("/wxarticle/list/{id}/{pageNo}/json")
    suspend fun listWeChatArticle(
        @Path("id") id: Int,
        @Path("pageNo") pageNo: Int
    ): WanResponse<Article>

    /**
     * 获取项目tab
     */
    @POST("/article/query/{pageNo}/json")
    suspend fun search(
        @Path("pageNo") pageNo: Int,
        @Query("k") k: String
    ): WanResponse<Article>

    /**
     * 体系
     */
    @GET("/tree/json")
    suspend fun getSystemList(): WanResponse<List<SystemList>>

    /**
     * 获取文章列表
     * @param pageNo 页码
     * @param cid 分类ID
     */
    @GET("/article/list/{pageNo}/json")
    suspend fun listArticle(
        @Path("pageNo") pageNo: Int,
        @Query("cid") cid: Int?
    ): WanResponse<PageList<Article>>

    /**
     * 导航
     */
    @GET("/navi/json")
    suspend fun getNavigation(): WanResponse<List<Navigation>>

    /**
     * 排名
     */
    @GET("/coin/rank/{pageNo}/json")
    suspend fun getRank(
        @Path("pageNo") pageNo: Int
    ): WanResponse<Rank>

    /**
     * 积分记录
     */
    @GET("/lg/coin/list/{pageNo}/json")
    suspend fun listIntegralRecord(
        @Path("pageNo") pageNo: Int
    ): WanResponse<IntegralRecord>

    /**
     * 我分享的文章
     */
    @GET("/user/lg/private_articles/{pageNo}/json")
    suspend fun listMyArticle(
        @Path("pageNo") pageNo: Int
    ): WanResponse<MyArticle>

    /**
     * 我分享的文章
     */
    @POST("/lg/user_article/delete/{id}/json")
    suspend fun deleteMyArticle(
        @Path("id") id: Int
    ): WanResponse<Any>

    /**
     * 分享文章
     */
    @POST("/lg/user_article/add/json")
    suspend fun shareArticle(
        @Query("title") title: String,
        @Query("link") link: String
    ): WanResponse<Any>

    /**
     * 注册
     */
    @POST("/user/register")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") rePassword: String
    ): WanResponse<Any>
}
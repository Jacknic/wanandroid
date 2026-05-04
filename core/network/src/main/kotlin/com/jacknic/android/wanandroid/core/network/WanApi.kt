package com.jacknic.android.wanandroid.core.network

import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Banner
import com.jacknic.android.wanandroid.core.model.Chapter
import com.jacknic.android.wanandroid.core.model.CoinInfo
import com.jacknic.android.wanandroid.core.model.CourseInfo
import com.jacknic.android.wanandroid.core.model.FriendLink
import com.jacknic.android.wanandroid.core.model.HotKeyword
import com.jacknic.android.wanandroid.core.model.NavInfo
import com.jacknic.android.wanandroid.core.model.Paging
import com.jacknic.android.wanandroid.core.model.PersonalInfo
import com.jacknic.android.wanandroid.core.model.Rank
import com.jacknic.android.wanandroid.core.model.ShareArticles
import com.jacknic.android.wanandroid.core.model.Tree
import com.jacknic.android.wanandroid.core.model.UserInfo
import com.jacknic.android.wanandroid.core.model.WanResult
import com.jacknic.android.wanandroid.core.model.WendaComment
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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
     * @param cid 教程ID (optional)
     * @param pageSize 分页大小 1-40
     */
    @PageNotice
    @GET("/article/list/{page}/json")
    suspend fun getArticleList(
        @Path("page") page: Int,
        @Query("cid") cid: String? = null,
        @Query("page_size") pageSize: Int
    ): WanResult<Paging<Article>>

    /**
     * 获取首页 banner 列表
     */
    @GET("/banner/json")
    suspend fun getBannerList(): WanResult<List<Banner>>

    /**
     * 常用网站
     */
    @GET("/friend/json")
    suspend fun getFriend(): WanResult<List<FriendLink>>

    /**
     * 搜索热词
     */
    @GET("/hotkey/json")
    suspend fun getHotkey(): WanResult<List<HotKeyword>>

    /**
     * 置顶文章
     */
    @GET("/article/top/json")
    suspend fun getArticleTop(): WanResult<List<Article>>

    /**
     * 体系数据
     */
    @GET("/tree/json")
    suspend fun getTree(): WanResult<List<Tree>>

    /**
     * 导航数据
     */
    @GET("/navi/json")
    suspend fun getNavi(): WanResult<List<NavInfo>>

    /**
     * 项目分类
     */
    @GET("/project/tree/json")
    suspend fun getProjectTree(): WanResult<List<Chapter>>

    /**
     * 项目列表数据
     *
     * @param page 页码
     * @param cid 分类ID (optional)
     */
    @GET("/project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int? = null
    ): WanResult<Paging<Article>>

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun postUserLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): WanResult<UserInfo>

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     * @param repassword 密码确认
     */
    @FormUrlEncoded
    @POST("/user/register")
    suspend fun postUserRegister(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): WanResult<UserInfo>

    /**
     * 退出
     */
    @GET("/user/logout/json")
    suspend fun getUserLogout(): WanResult<Any?>

    /**
     * 收藏文章列表
     *
     * @param page 页码
     */
    @GET("/lg/collect/list/{page}/json")
    suspend fun getLgCollectList(@Path("page") page: Int): WanResult<Paging<Article>>

    /**
     * 收藏站内文章
     *
     * @param articleId 文章ID
     */
    @GET("/collect/{article_id}/json")
    suspend fun getCollectArticle(@Path("article_id") articleId: Int): WanResult<Any?>

    /**
     * 收藏站外文章
     *
     * @param title 标题
     * @param author 作者
     * @param link 链接地址
     */
    @FormUrlEncoded
    @POST("/lg/collect/add/json")
    suspend fun postLgCollectAdd(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): WanResult<UserInfo>

    /**
     * 编辑收藏的文章
     *
     * @param articleId 文章ID
     * @param title 标题
     * @param author 作者
     * @param link 链接地址
     */
    @FormUrlEncoded
    @POST("/lg/collect/user_article/update/{article_id}/json")
    suspend fun postLgCollectUserArticleUpdate(
        @Path("article_id") articleId: Int,
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): WanResult<UserInfo>

    /**
     * 取消收藏-文章列表
     *
     * @param articleId 文章ID
     */
    @POST("/lg/uncollect_originId/{article_id}/json")
    suspend fun postLgUncollectOriginId(@Path("article_id") articleId: Int): WanResult<Any?>

    /**
     * 取消收藏-我的收藏页面
     *
     * @param collecId 收藏ID
     */
    @POST("/lg/uncollect/{collec_id}/json")
    suspend fun postLgUncollect(@Path("collec_id") collecId: Int): WanResult<Any?>

    /**
     * 收藏网站列表
     */
    @GET("/lg/collect/usertools/json")
    suspend fun getLgCollectUsertools(): WanResult<Any?>

    /**
     * 收藏网址
     *
     * @param name 网站名称
     * @param link 链接地址
     */
    @FormUrlEncoded
    @POST("/lg/collect/addtool/json")
    suspend fun postLgCollectAddtool(
        @Field("name") name: String,
        @Field("link") link: String
    ): WanResult<UserInfo>

    /**
     * 编辑收藏网址
     *
     * @param id 收藏ID
     * @param name 网站名称
     * @param link 链接地址
     */
    @FormUrlEncoded
    @POST("/lg/collect/updatetool/json")
    suspend fun postLgCollectUpdatetool(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("link") link: String
    ): WanResult<UserInfo>

    /**
     * 删除收藏网址
     *
     * @param id 收藏ID
     */
    @FormUrlEncoded
    @POST("/lg/collect/deletetool/json")
    suspend fun postLgCollectDeletetool(@Field("id") id: Int): WanResult<Any?>

    /**
     * 搜索
     *
     * @param page 页码
     * @param k 关键词
     * @param pageSize 分页大小 1-40 (optional)
     */
    @POST("/article/query/{page}/json")
    suspend fun postArticleQuery(
        @Path("page") page: Int,
        @Query("k") k: String,
        @Query("page_size") pageSize: Int? = null
    ): WanResult<Paging<Article>>

    /**
     * 积分排行榜
     *
     * @param page 页码
     */
    @GET("/coin/rank/{page}/json")
    suspend fun getCoinRank(@Path("page") page: Int): WanResult<Paging<Rank>>

    /**
     * 获取个人积分
     */
    @GET("/lg/coin/userinfo/json")
    suspend fun getLgCoinUserinfo(): WanResult<CoinInfo>

    /**
     * 获取个人积分获取列表
     *
     * @param page 页码
     */
    @GET("/lg/coin/list/{page}/json")
    suspend fun getLgCoinList(@Path("page") page: Int): WanResult<Paging<Any?>>

    /**
     * 广场列表数据
     *
     * @param page 页码
     * @param pageSize 分页大小1-40 (optional)
     */
    @GET("/user_article/list/{page}/json")
    suspend fun getUserArticleList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int? = null
    ): WanResult<Paging<Article>>

    /**
     * 分享人对应列表数据
     *
     * @param userId 用户id
     * @param page 页码
     */
    @GET("/user/{user_id}/share_articles/{page}/json")
    suspend fun getUserShareArticles(
        @Path("user_id") userId: Int,
        @Path("page") page: Int
    ): WanResult<ShareArticles>

    /**
     * 自己的分享的文章列表
     *
     * @param page 页码
     * @param pageSize 分页大小 1-40 (optional)
     */
    @GET("/user/lg/private_articles/{page}/json")
    suspend fun getUserLgPrivateArticles(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int? = null
    ): WanResult<Paging<Article>>

    /**
     * 分享文章
     *
     * @param title 标题
     * @param link 链接地址
     */
    @FormUrlEncoded
    @POST("/lg/user_article/add/json")
    suspend fun postLgUserArticleAdd(
        @Field("title") title: String,
        @Field("link") link: String
    ): WanResult<UserInfo>

    /**
     * 删除自己分享的文章
     *
     * @param articleId 文章id
     */
    @POST("/lg/user_article/delete/{article_id}/json")
    suspend fun postLgUserArticleDelete(@Path("article_id") articleId: Int): WanResult<Any?>

    /**
     * 个人信息接口
     */
    @GET("/user/lg/userinfo/json")
    suspend fun getUserLgUserinfo(): WanResult<PersonalInfo>

    /**
     * 未读消息数量
     */
    @GET("/message/lg/count_unread/json")
    suspend fun getMessageLgCountUnread(): WanResult<Int>

    /**
     * 已读消息列表
     *
     * @param page 页码
     * @param pageSize 分页大小 1-40 (optional)
     */
    @GET("/message/lg/readed_list/{page}/json")
    suspend fun getMessageLgReadedList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int? = null
    ): WanResult<Paging<Any?>>

    /**
     * 未读消息列表
     *
     * @param page 页码
     */
    @GET("/message/lg/unreaded_list/{page}/json")
    suspend fun getMessageLgUnreadedList(@Path("page") page: Int): WanResult<Paging<Any?>>

    /**
     * 问答列表
     *
     * @param page 页码
     * @param pageSize 分页大小 (optional)
     */
    @GET("/wenda/list/{page}/json")
    suspend fun getWendaList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int? = null
    ): WanResult<Paging<Any?>>

    /**
     * 问答评论列表
     *
     * @param wendaId 问答ID
     * @param page 页码 (optional)
     * @param pageSize 分页大小 1-200 (optional)
     */
    @GET("/wenda/comments/{wenda_id}/json")
    suspend fun getWendaComments(
        @Path("wenda_id") wendaId: Int,
        @Path("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): WanResult<Paging<WendaComment>>

    /**
     * 获取公众号列表
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getWxarticleChapters(): WanResult<Any?>

    /**
     * 获取公众号历史文章
     *
     * @param wxId 公众号 ID
     * @param page 页码
     * @param k 搜索关键词 (optional)
     */
    @GET("/wxarticle/list/{wx_id}/{page}/json")
    suspend fun getWxArticleList(
        @Path("wx_id") wxId: Int,
        @Path("page") page: Int,
        @Query("k") k: String? = null
    ): WanResult<Paging<Article>>

    /**
     * 教程列表
     */
    @GET("/chapter/547/sublist/json")
    suspend fun getChapterSublist(): WanResult<List<CourseInfo>>

}
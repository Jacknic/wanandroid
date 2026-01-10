package com.jacknic.android.wanandroid.core.network

import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Banner
import com.jacknic.android.wanandroid.core.model.FriendLink
import com.jacknic.android.wanandroid.core.model.HotKeyword
import com.jacknic.android.wanandroid.core.model.Paging
import com.jacknic.android.wanandroid.core.model.UserInfo
import com.jacknic.android.wanandroid.core.model.WanResult
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
     * @param pageSize 分页大小
     */
    @PageNotice
    @GET("/article/list/{page}/json")
    suspend fun getArticleList(
        @Path("page") page: Int, @Query("page_size") pageSize: Int
    ): WanResult<Paging<Article>>

    /**
     * 获取首页 banner 列表
     */
    @GET("/banner/json")
    suspend fun getBannerList(): WanResult<List<Banner>>


    /**
     * 单个教程下所有文章列表
     *
     * 列表接口返回的教程合集，注意教程对象中有个 id 需要在此接口使用。
     * @param page 页码
     * @param pageSize 分页大小 1-40
     * @param cid 教程ID (optional)
     */
    @GET("/article/list/{page}/json")
    suspend fun getArticleList(
        @Path("page") page: Int, @Query("pageSize") pageSize: Int, @Query("cid") cid: String? = null
    ): WanResult<Paging<Article>>

    /**
     * 最新项目列表
     *
     * 最新项目列表
     * @param page 页码
     * @param pageSize 分页大小 1-40 (optional)
     */
    @GET("/article/listproject/{page}/json")
    suspend fun getArticleListProject(
        @Path("page") page: Int, @Query("pageSize") pageSize: Int
    ): WanResult<Paging<Article>>

    /**
     * 搜索
     *
     * 搜索
     * @param page 页码
     * @param k 关键词
     * @param pageSize 分页大小 1-40 (optional)
     */
    @GET("/article/query/{page}/json")
    suspend fun postArticleQueryPage(
        @Path("page") page: Int, @Query("k") k: String, @Query("pageSize") pageSize: Int
    ): WanResult<Paging<Article>>

    /**
     * 置顶文章
     *
     * 置顶文章
     */
    @GET("/article/top/json")
    suspend fun getArticleTop(): WanResult<List<Article>>

    /**
     *  教程列表
     *
     * 教程列表，GET请求，直接访问即可；547是固定值不会变化。
     */
    @GET("/chapter/547/sublist/json")
    suspend fun getChapterSublist(): WanResult<Paging<Article>>

    /**
     * 积分排行榜
     *
     *
     * @param page 页码
     */
    @GET("/coin/rank/{page}/json")
    suspend fun getCoinRank(@Path("page") page: Int): WanResult<Paging<Article>>

    /**
     * 收藏站内文章
     *
     * 收藏站内文章
     * @param articleId 文章ID
     */
    @GET("/collect/{article_id}/json")
    suspend fun getCollectArticleId(articleId: Int): WanResult<Paging<Article>>

    /**
     * 常用网站
     *
     * 常用网站
     */
    @GET("/friend/json")
    suspend fun getFriend(): WanResult<List<FriendLink>>

    /**
     * 搜索热词
     *
     * 搜索热词
     */
    @GET("/hotkey/json")
    suspend fun getHotkey(): WanResult<List<HotKeyword>>

    /**
     * 获取个人积分获取列表
     *
     * 获取个人积分，需要登录后访问
     * @param page 页码
     */
    @GET("/lg/coin/list/{page}/json")
    suspend fun getLgCoinList(@Path("page") page: Int): WanResult<Paging<Article>>

    /**
     * 获取个人积分
     *
     * 获取个人积分，需要登录后访问
     */
    @GET("/lg/coin/userinfo/json")
    suspend fun getLgCoinUserinfo(): WanResult<Paging<Article>>

    /**
     * 收藏站外文章
     *
     * 收藏站外文章
     * @param title 标题 (optional)
     * @param author 作者 (optional)
     * @param link 链接地址 (optional)
     */
    @POST("/lg/collect/add/json")
    suspend fun postLgCollectAdd(
        title: String, author: String, link: String
    ): WanResult<Paging<Article>>

    /**
     * 收藏网址
     *
     * 收藏网址
     * @param name 网站名称 (optional)
     * @param link 链接地址 (optional)
     */
    @POST("/lg/collect/addtool/json")
    suspend fun postLgCollectAddtool(name: String, link: String): WanResult<Paging<Article>>

    /**
     * 删除收藏网址
     *
     * 删除收藏网址
     * @param id 收藏ID (optional)
     */
    @POST("/lg/collect/deletetool/json")
    suspend fun postLgCollectDeletetool(id: Int): WanResult<Paging<Article>>

    /**
     * 收藏文章列表
     *
     * 收藏文章列表
     * @param page 页码
     */
    @GET("/lg/collect/list/{page}/json")
    suspend fun getLgCollectList(@Path("page") page: Int): WanResult<Paging<Article>>

    /**
     * 编辑收藏网址
     *
     * 编辑收藏网址
     * @param name 网站名称 (optional)
     * @param link 链接地址 (optional)
     * @param id 收藏ID (optional)
     */
    @POST("/lg/collect/updatetool/json")
    suspend fun postLgCollectUpdatetool(
        name: String, link: String, id: Int
    ): WanResult<Paging<Article>>

    /**
     * 编辑收藏的文章
     *
     * 编辑收藏的文章
     * @param articleId 文章ID
     * @param title 标题 (optional)
     * @param author 作者 (optional)
     * @param link 链接地址 (optional)
     */
    @GET("/lg/collect/user_article/update/{article_id}/json")
    suspend fun lgCollectUserArticleUpdateArticle(
        articleId: Int, title: String, author: String, link: String
    ): WanResult<Paging<Article>>

    /**
     *  收藏网站列表
     *
     * 收藏网站列表
     */
    @GET("/lg/collect/usertools/json")
    suspend fun getLgCollectUsertools(): WanResult<Paging<Article>>

    /**
     * 取消收藏-我的收藏页面
     *
     * 取消收藏-我的收藏页面
     * @param collecId 收藏ID
     */
    @GET("/lg/uncollect/{collec_id}/json")
    suspend fun lgUncollectCollec(collecId: Int): WanResult<Paging<Article>>

    /**
     * 取消收藏-文章列表
     *
     * 取消收藏-文章列表
     * @param articleId 文章ID
     */
    @GET("/lg/uncollect_originId/{article_id}/json")
    suspend fun lgUncollectOriginIdArticle(articleId: Int): WanResult<Paging<Article>>

    /**
     * 分享文章
     *
     * 注意需要登录后查看，如果为CSDN，简书等链接会直接通过审核，在对外的分享文章列表中展示。
     * @param title 标题
     * @param link 链接地址
     */
    @POST("/lg/user_article/add/json")
    suspend fun postLgUserArticleAdd(title: String, link: String): WanResult<Paging<Article>>

    /**
     * 删除自己分享的文章
     *
     * 建议测试方式：登陆网站后，自己分享一篇文章在广场，然后与删除按钮，打开chrome调试模式，查看Network里面有请求。
     * @param articleId 文章id
     */
    @GET("/lg/user_article/delete/{article_id}/json")
    suspend fun lgUserArticleDeleteArticle(articleId: Int): WanResult<Paging<Article>>

    /**
     * 未读消息数量
     *
     * 返回当前登录用户未读消息数量。
     */
    @GET("/message/lg/count_unread/json")
    suspend fun getMessageLgCountUnread(): WanResult<Paging<Article>>

    /**
     * 已读消息列表
     *
     * 返回当前已经登录用户已读消息列表，分页展示。
     * @param page 页码
     * @param pageSize 分页大小 1-40 (optional)
     */
    @GET("/message/lg/readed_list/{page}/json")
    suspend fun getMessageLgReadedList(
        @Path("page") page: Int, @Query("pageSize") pageSize: Int
    ): WanResult<Paging<Article>>

    /**
     * 未读消息列表
     *
     * 返回当前登录用户未读消息列表，分页展示。
     *
     * 注意1：此接口需要登录。
     *
     * 注意 2：此接口一旦访问，则所有该用户的消息都会被认为已读，即第二次只能从已读消息列表获取，
     * 想获取未读数量不要访问此接口。 注：该接口支持传入 page_size 控制分页数量，取值为[1-40]，
     * 不传则使用默认值，一旦传入了 page_size，后续该接口分页都需要带上，否则会造成分页读取错误。
     * @param page 页码
     */
    @GET("/message/lg/unreaded_list/{page}/json")
    suspend fun getMessageLgUnreadedList(@Path("page") page: Int): WanResult<Paging<Article>>

    /**
     * 导航数据
     *
     * 导航数据
     */
    @GET("/navi/json")
    suspend fun getNavi(): WanResult<Paging<Article>>

    /**
     * 项目列表数据
     *
     * 项目列表数据
     * @param page
     * @param cid 分类ID (optional)
     */
    @GET("/project/list/{page}/json")
    suspend fun getProjectList(page: String, cid: Int): WanResult<Paging<Article>>

    /**
     * 项目分类
     *
     * 项目分类
     */
    @GET("/project/tree/json")
    suspend fun getProjectTree(): WanResult<Paging<Article>>

    /**
     * 体系数据
     *
     * 体系数据
     */
    @GET("/tree/json")
    suspend fun getTree(): WanResult<Paging<Article>>

    /**
     * 广场列表数据
     *
     * 该接口支持传入 page_size 控制分页数量，取值为[1-40]，
     * 不传则使用默认值，一旦传入了 page_size，后续该接口分页都需要带上，否则会造成分页读取错误。
     * @param page 页码
     * @param pageSize 分页大小1-40 (optional)
     */
    @GET("/user_article/list/{page}/json")
    suspend fun getUserArticleList(
        @Path("page") page: Int, @Query("pageSize") pageSize: Int
    ): WanResult<Paging<Article>>

    /**
     * 自己的分享的文章列表
     *
     * 该接口支持传入 page_size 控制分页数量，取值为 [1-40]，
     * 不传则使用默认值，一旦传入了 page_size，后续该接口分页都需要带上，否则会造成分页读取错误。
     * @param page 页码
     * @param pageSize 分页大小 1-40 (optional)
     */
    @GET("/user/lg/private_articles/{page}/json")
    suspend fun getUserLgPrivateArticles(
        @Path("page") page: Int, @Query("pageSize") pageSize: Int
    ): WanResult<Paging<Article>>

    /**
     * 个人信息接口
     *
     * 个人信息接口
     */
    @GET("/user/lg/userinfo/json")
    suspend fun getUserLgUserinfo(): WanResult<Paging<Article>>

    /**
     * 登录
     *
     *
     * 登录后会在cookie中返回账号密码，只要在客户端做cookie持久化存储即可自动登录验证
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
     * 退出
     *
     * 访问了 logout 后，服务端会让客户端清除 Cookie（即cookie max-Age&#x3D;0），如果客户端 Cookie 实现合理，可以实现自动清理，如果本地做了用户账号密码和保存，及时清理。
     */
    @GET("/user/logout/json")
    suspend fun getUserLogout(): WanResult<Paging<Article>>

    /**
     * 注册
     *
     * 注册用户
     * @param username 用户名
     * @param password 密码
     * @param repassword 密码确认
     */
    @POST("/user/register")
    suspend fun postUserRegister(
        username: String, password: String, repassword: String
    ): WanResult<Paging<Article>>

    /**
     * 分享人对应列表数据
     *
     * 这个展示的文章数据都是审核通过的，一般是点击分享人然后展示的列表。
     * @param userId 用户id
     * @param page 页码
     */
    @GET("/user/{user_id}/share_articles/{page}/json")
    suspend fun getUserUserIdShareArticles(
        @Path("user_id") userId: Int, @Path("page") page: Int
    ): WanResult<Paging<Article>>

    /**
     * 问答评论列表
     *
     * 问答评论列表
     * @param wendaId 问答ID
     * @param page 页码 (optional)
     * @param pageSize 分页大小 1-200 (optional)
     */
    @GET("/wenda/comments/{wenda_id}/json")
    suspend fun getWendaCommentsWendaId(
        wendaId: Int, @Path("page") page: Int, @Query("pageSize") pageSize: Int
    ): WanResult<Paging<Article>>

    /**
     * 问答列表
     *
     * 问答列表
     * @param page 页码
     * @param pageSize 分页大小 (optional)
     */
    @GET("/wenda/list/{page}/json")
    suspend fun getWendaList(
        @Path("page") page: Int, @Query("pageSize") pageSize: Int
    ): WanResult<Paging<Article>>

    /**
     * 获取公众号列表
     *
     * 获取公众号列表
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getWxarticleChapters(): WanResult<Paging<Article>>

    /**
     * 在某个公众号中搜索历史文章
     *
     * 在某个公众号中搜索历史文章
     * @param wxId 公众号 ID
     * @param page 页码
     * @param pageSize 分页大小 1-40 (optional)
     * @param k 搜索关键词 (optional)
     */
    @GET("/wxarticle/list/{wx_id}/{page}/json")
    suspend fun getWxArticleList(
        wxId: Int, @Path("page") page: Int, @Query("pageSize") pageSize: Int, @Query("k") k: String
    ): WanResult<Paging<Article>>

}
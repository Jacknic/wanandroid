package com.jacknic.android.wanandroid.core.domain

import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Banner
import com.jacknic.android.wanandroid.core.model.CoinInfo
import com.jacknic.android.wanandroid.core.model.CourseInfo
import com.jacknic.android.wanandroid.core.model.FriendLink
import com.jacknic.android.wanandroid.core.model.HotKeyword
import com.jacknic.android.wanandroid.core.model.NavInfo
import com.jacknic.android.wanandroid.core.model.Paging
import com.jacknic.android.wanandroid.core.model.PersonalInfo
import com.jacknic.android.wanandroid.core.model.ProjectTree
import com.jacknic.android.wanandroid.core.model.Rank
import com.jacknic.android.wanandroid.core.model.ShareArticles
import com.jacknic.android.wanandroid.core.model.Tree
import com.jacknic.android.wanandroid.core.model.UserInfo
import com.jacknic.android.wanandroid.core.model.WendaComment

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
     * @param cid 教程ID (optional)
     */
    suspend fun getHomeArticleList(page: Int, pageSize: Int, cid: String? = null): Result<Paging<Article>>

    /**
     * 获取首页 banner 列表
     */
    suspend fun getHomeBannerList(): Result<List<Banner>>

    /**
     * 获取常用网站
     */
    suspend fun getFriend(): Result<List<FriendLink>>

    /**
     * 获取搜索热词
     */
    suspend fun getHotkey(): Result<List<HotKeyword>>

    /**
     * 获取置顶文章
     */
    suspend fun getArticleTop(): Result<List<Article>>

    /**
     * 获取体系数据
     */
    suspend fun getTree(): Result<List<Tree>>

    /**
     * 获取导航数据
     */
    suspend fun getNavi(): Result<List<NavInfo>>

    /**
     * 获取项目分类
     */
    suspend fun getProjectTree(): Result<List<ProjectTree>>

    /**
     * 获取项目列表数据
     *
     * @param page 页码
     * @param cid 分类ID (optional)
     */
    suspend fun getProjectList(page: Int, cid: Int? = null): Result<Paging<Article>>

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     */
    suspend fun login(username: String, password: String): Result<UserInfo>

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param repassword 密码确认
     */
    suspend fun register(username: String, password: String, repassword: String): Result<Any?>

    /**
     * 用户登出
     */
    suspend fun logout(): Result<Any?>

    /**
     * 获取收藏文章列表
     *
     * @param page 页码
     */
    suspend fun getLgCollectList(page: Int): Result<Paging<Article>>

    /**
     * 收藏站内文章
     *
     * @param articleId 文章ID
     */
    suspend fun collectArticle(articleId: Int): Result<Any?>

    /**
     * 收藏站外文章
     *
     * @param title 标题
     * @param author 作者
     * @param link 链接地址
     */
    suspend fun addCollect(title: String, author: String, link: String): Result<Any?>

    /**
     * 编辑收藏的文章
     *
     * @param articleId 文章ID
     * @param title 标题
     * @param author 作者
     * @param link 链接地址
     */
    suspend fun updateCollectArticle(articleId: Int, title: String, author: String, link: String): Result<Any?>

    /**
     * 取消收藏-文章列表
     *
     * @param articleId 文章ID
     */
    suspend fun uncollectOriginId(articleId: Int): Result<Any?>

    /**
     * 取消收藏-我的收藏页面
     *
     * @param collecId 收藏ID
     */
    suspend fun uncollect(collecId: Int): Result<Any?>

    /**
     * 获取收藏网站列表
     */
    suspend fun getLgCollectUsertools(): Result<Any?>

    /**
     * 收藏网址
     *
     * @param name 网站名称
     * @param link 链接地址
     */
    suspend fun addCollectTool(name: String, link: String): Result<Any?>

    /**
     * 编辑收藏网址
     *
     * @param id 收藏ID
     * @param name 网站名称
     * @param link 链接地址
     */
    suspend fun updateCollectTool(id: Int, name: String, link: String): Result<Any?>

    /**
     * 删除收藏网址
     *
     * @param id 收藏ID
     */
    suspend fun deleteCollectTool(id: Int): Result<Any?>

    /**
     * 搜索
     *
     * @param page 页码
     * @param k 关键词
     * @param pageSize 分页大小 1-40 (optional)
     */
    suspend fun searchArticles(page: Int, k: String, pageSize: Int? = null): Result<Paging<Article>>

    /**
     * 获取积分排行榜
     *
     * @param page 页码
     */
    suspend fun getCoinRank(page: Int): Result<Paging<Rank>>

    /**
     * 获取个人积分
     */
    suspend fun getLgCoinUserinfo(): Result<CoinInfo>

    /**
     * 获取个人积分获取列表
     *
     * @param page 页码
     */
    suspend fun getLgCoinList(page: Int): Result<Paging<Any?>>

    /**
     * 获取广场列表数据
     *
     * @param page 页码
     * @param pageSize 分页大小1-40 (optional)
     */
    suspend fun getUserArticleList(page: Int, pageSize: Int? = null): Result<Paging<Article>>

    /**
     * 获取分享人对应列表数据
     *
     * @param userId 用户id
     * @param page 页码
     */
    suspend fun getUserShareArticles(userId: Int, page: Int): Result<ShareArticles>

    /**
     * 获取自己的分享的文章列表
     *
     * @param page 页码
     * @param pageSize 分页大小 1-40 (optional)
     */
    suspend fun getUserLgPrivateArticles(page: Int, pageSize: Int? = null): Result<Paging<Article>>

    /**
     * 分享文章
     *
     * @param title 标题
     * @param link 链接地址
     */
    suspend fun shareArticle(title: String, link: String): Result<Any?>

    /**
     * 删除自己分享的文章
     *
     * @param articleId 文章id
     */
    suspend fun deleteSharedArticle(articleId: Int): Result<Any?>

    /**
     * 获取个人信息接口
     */
    suspend fun getUserLgUserinfo(): Result<PersonalInfo>

    /**
     * 获取未读消息数量
     */
    suspend fun getMessageLgCountUnread(): Result<Int>

    /**
     * 获取已读消息列表
     *
     * @param page 页码
     * @param pageSize 分页大小 1-40 (optional)
     */
    suspend fun getMessageLgReadedList(page: Int, pageSize: Int? = null): Result<Paging<Any?>>

    /**
     * 获取未读消息列表
     *
     * @param page 页码
     */
    suspend fun getMessageLgUnreadedList(page: Int): Result<Paging<Any?>>

    /**
     * 获取问答列表
     *
     * @param page 页码
     * @param pageSize 分页大小 (optional)
     */
    suspend fun getWendaList(page: Int, pageSize: Int? = null): Result<Paging<Any?>>

    /**
     * 获取问答评论列表
     *
     * @param wendaId 问答ID
     * @param page 页码 (optional)
     * @param pageSize 分页大小 1-200 (optional)
     */
    suspend fun getWendaComments(wendaId: Int, page: Int? = null, pageSize: Int? = null): Result<Paging<WendaComment>>

    /**
     * 获取公众号列表
     */
    suspend fun getWxarticleChapters(): Result<Any?>

    /**
     * 获取公众号历史文章
     *
     * @param wxId 公众号 ID
     * @param page 页码
     * @param k 搜索关键词 (optional)
     */
    suspend fun getWxArticleList(wxId: Int, page: Int, k: String? = null): Result<Paging<Article>>

    /**
     * 获取教程列表
     */
    suspend fun getChapterSublist(): Result<List<CourseInfo>>
}
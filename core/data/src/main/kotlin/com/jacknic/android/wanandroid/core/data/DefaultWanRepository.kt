package com.jacknic.android.wanandroid.core.data

import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.network.AndroidCookieJar
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

    override suspend fun getHomeArticleList(page: Int, pageSize: Int, cid: String?) = runResult {
        api.getArticleList(page, cid, pageSize)
    }

    override suspend fun getHomeBannerList() = runResult(api::getBannerList)

    override suspend fun getFriend() = runResult(api::getFriend)

    override suspend fun getHotkey() = runResult(api::getHotkey)

    override suspend fun getArticleTop() = runResult(api::getArticleTop)

    override suspend fun getTree() = runResult(api::getTree)

    override suspend fun getNavi() = runResult(api::getNavi)

    override suspend fun getProjectTree() = runResult(api::getProjectTree)

    override suspend fun getProjectList(page: Int, cid: Int?) = runResult {
        api.getProjectList(page, cid)
    }

    override suspend fun login(username: String, password: String) = runResult {
        api.postUserLogin(username, password)
    }

    override suspend fun register(username: String, password: String, repassword: String) = runResult {
        api.postUserRegister(username, password, repassword)
    }

    override suspend fun logout() = runResult {
        AndroidCookieJar.clear()
        api.getUserLogout()
    }

    override suspend fun getLgCollectList(page: Int) = runResult {
        api.getLgCollectList(page)
    }

    override suspend fun collectArticle(articleId: Int) = runResult {
        api.postCollectArticle(articleId)
    }

    override suspend fun addCollect(title: String, author: String, link: String) = runResult {
        api.postLgCollectAdd(title, author, link)
    }

    override suspend fun updateCollectArticle(articleId: Int, title: String, author: String, link: String) = runResult {
        api.postLgCollectUserArticleUpdate(articleId, title, author, link)
    }

    override suspend fun uncollectOriginId(articleId: Int) = runResult {
        api.postLgUncollectOriginId(articleId)
    }

    override suspend fun uncollect(collecId: Int) = runResult {
        api.postLgUncollect(collecId)
    }

    override suspend fun getLgCollectUsertools() = runResult(api::getLgCollectUsertools)

    override suspend fun addCollectTool(name: String, link: String) = runResult {
        api.postLgCollectAddtool(name, link)
    }

    override suspend fun updateCollectTool(id: Int, name: String, link: String) = runResult {
        api.postLgCollectUpdatetool(id, name, link)
    }

    override suspend fun deleteCollectTool(id: Int) = runResult {
        api.postLgCollectDeletetool(id)
    }

    override suspend fun searchArticles(page: Int, k: String, pageSize: Int?) = runResult {
        api.postArticleQuery(page, k, pageSize)
    }

    override suspend fun getCoinRank(page: Int) = runResult {
        api.getCoinRank(page)
    }

    override suspend fun getLgCoinUserinfo() = runResult(api::getLgCoinUserinfo)

    override suspend fun getLgCoinList(page: Int) = runResult {
        api.getLgCoinList(page)
    }

    override suspend fun getUserArticleList(page: Int, pageSize: Int?) = runResult {
        api.getUserArticleList(page, pageSize)
    }

    override suspend fun getUserShareArticles(userId: Int, page: Int) = runResult {
        api.getUserShareArticles(userId, page)
    }

    override suspend fun getUserLgPrivateArticles(page: Int, pageSize: Int?) = runResult {
        api.getUserLgPrivateArticles(page, pageSize)
    }

    override suspend fun shareArticle(title: String, link: String) = runResult {
        api.postLgUserArticleAdd(title, link)
    }

    override suspend fun deleteSharedArticle(articleId: Int) = runResult {
        api.postLgUserArticleDelete(articleId)
    }

    override suspend fun getUserLgUserinfo() = runResult(api::getUserLgUserinfo)

    override suspend fun getMessageLgCountUnread() = runResult(api::getMessageLgCountUnread)

    override suspend fun getMessageLgReadedList(page: Int, pageSize: Int?) = runResult {
        api.getMessageLgReadedList(page, pageSize)
    }

    override suspend fun getMessageLgUnreadedList(page: Int) = runResult {
        api.getMessageLgUnreadedList(page)
    }

    override suspend fun getWendaList(page: Int, pageSize: Int?) = runResult {
        api.getWendaList(page, pageSize)
    }

    override suspend fun getWendaComments(wendaId: Int, page: Int?, pageSize: Int?) = runResult {
        api.getWendaComments(wendaId, page, pageSize)
    }

    override suspend fun getWxarticleChapters() = runResult(api::getWxarticleChapters)

    override suspend fun getWxArticleList(wxId: Int, page: Int, k: String?) = runResult {
        api.getWxArticleList(wxId, page, k)
    }

    override suspend fun getChapterSublist() = runResult(api::getChapterSublist)

    override suspend fun addTodo(
        title: String,
        content: String,
        date: String?,
        type: Int?,
        priority: Int?
    ) = runResult {
        api.postLgTodoAdd(title, content, date, type, priority)
    }

    override suspend fun updateTodo(
        id: Int,
        title: String,
        content: String,
        date: String,
        status: Int,
        type: Int?,
        priority: Int?
    ) = runResult {
        api.postLgTodoUpdate(id, title, content, date, status, type, priority)
    }

    override suspend fun deleteTodo(id: Int) = runResult {
        api.postLgTodoDelete(id)
    }

    override suspend fun doneTodo(id: Int, status: Int) = runResult {
        api.postLgTodoDone(id, status)
    }

    override suspend fun getTodoList(
        page: Int,
        status: Int?,
        type: Int?,
        priority: Int?,
        orderBy: Int?
    ) = runResult {
        api.getLgTodoList(page, status, type, priority, orderBy)
    }
}

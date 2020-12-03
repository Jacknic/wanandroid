package com.jacknic.wanandroid.data.remote.source

import com.jacknic.wanandroid.data.remote.api.WanApi
import com.jacknic.wanandroid.data.util.wanResult

/**
 * 玩安卓服务端数据源
 *
 * @author Jacknic
 */
class WanDataSource(private val wanApi: WanApi) {

    suspend fun listBanner() = wanApi.listBanner()
        .wanResult("获取首页banner失败！")

    suspend fun register(username: String, password: String, rePassword: String) =
        wanApi.register(username, password, rePassword)
            .wanResult("获取失败！")

    suspend fun login(username: String, password: String) =
        wanApi.login(username, password)
            .wanResult("登录失败！")

    suspend fun logout() = wanApi.logout()
        .wanResult("退出登录失败！")

    suspend fun getTopList() = wanApi.getTopList()
        .wanResult("获取置顶文章列表失败！")

    suspend fun listCollect(pageNo: Int) = wanApi.listCollect(pageNo)
        .wanResult("获取收藏列表失败！")

    suspend fun getIntegral() = wanApi.getIntegral()
        .wanResult("获取个人积分失败！")

    suspend fun collect(id: Int) = wanApi.collect(id)
        .wanResult("收藏文章失败！")

    suspend fun unCollect(id: Int) = wanApi.unCollect(id)
        .wanResult("取消收藏失败！")

    suspend fun fetchProjectTabList() = wanApi.listProjectTab()
        .wanResult("获取失败！")

    suspend fun listAccountTab() = wanApi.listAccountTab()
        .wanResult("获取失败！")

    suspend fun listProject(pageNo: Int, cid: Int) = wanApi.listProject(pageNo, cid)
        .wanResult("获取失败！")

    suspend fun listWeChatArticle(cid: Int, pageNo: Int) = wanApi.listWeChatArticle(cid, pageNo)
        .wanResult("获取失败！")

    suspend fun search(pageNo: Int, k: String) = wanApi.search(pageNo, k)
        .wanResult("搜索错误！")

    suspend fun getSystemList() = wanApi.getSystemList()
        .wanResult("获取失败！")

    suspend fun listArticle(pageNo: Int, cid: Int? = null) = wanApi.listArticle(pageNo, cid)
        .wanResult("获取失败！")

    suspend fun fetchNavigation() = wanApi.getNavigation()
        .wanResult("获取失败！")

    suspend fun fetchRank(pageNo: Int) = wanApi.getRank(pageNo)
        .wanResult("获取失败！")

    suspend fun listIntegralRecord(pageNo: Int) = wanApi.listIntegralRecord(pageNo)
        .wanResult("获取失败！")

    suspend fun listMyArticle(pageNo: Int) = wanApi.listMyArticle(pageNo)
        .wanResult("获取我的文章列表失败！")

    suspend fun deleteMyArticle(id: Int) = wanApi.deleteMyArticle(id)
        .wanResult("删除失败！")

    suspend fun shareArticle(title: String, link: String) = wanApi.shareArticle(title, link)
        .wanResult("分享失败！")
}
package com.jacknic.wanandroid.data.model

/**
 * 我的文章
 */
class MyArticle {

    var coinInfo: CoinInfoBean? = null
    var shareArticles: PageList<Article>? = null

    class CoinInfoBean {
        var coinCount = 0
        var level = 0
        var rank = 0
        var userId = 0
        var username: String? = null
    }
}
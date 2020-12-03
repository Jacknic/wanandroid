package com.jacknic.wanandroid.data.model

class PageList<Item : Any> {
    /**
     * curPage : 2
     * datas : []
     * offset : 20
     * over : false
     * pageCount : 403
     * size : 20
     * total : 8054
     */
    var curPage = 0
    var offset = 0
    var over = false
    var pageCount = 0
    var size = 0
    var total = 0
    var datas: List<Item> = emptyList()
}
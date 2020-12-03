package com.jacknic.wanandroid.data.model

class User {

    /**
     * admin : false
     * chapterTops : []
     * collectIds : [10479,12202,12148,10916,12175]
     * email :
     * icon :
     * id : 36628
     * nickname : 18616720137
     * password :
     * publicName : 18616720137
     * token :
     * type : 0
     * username : 18616720137
     */

    var admin: Boolean = false
    var email = ""
    var icon = ""
    var id: Int = 0
    var nickname = ""
    var password = ""
    var publicName = ""
    var token = ""
    var type: Int = 0
    var username = ""
    var chapterTops: List<*>? = null
    var collectIds: List<Int>? = null
}

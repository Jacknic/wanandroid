package com.jacknic.wanandroid.data.remote.source

import com.jacknic.wanandroid.data.remote.api.AppUpdateApi
import com.jacknic.wanandroid.data.util.safeApiCall
import com.jacknic.wanandroid.data.util.safeResult

/**
 * @author Jacknic
 */
class AppUpdateDataSource(private val appApi: AppUpdateApi) {

    suspend fun check() = safeApiCall {
        appApi.checkVersion().safeResult("检测更新失败")
    }

}
package com.jacknic.wanandroid.data.remote.repository

import com.jacknic.wanandroid.data.remote.source.AppUpdateDataSource

/**
 * 应用接口
 *
 * @author Jacknic
 */
class AppUpdateRepository(private val source: AppUpdateDataSource) {

    suspend fun check() = source.check()
}
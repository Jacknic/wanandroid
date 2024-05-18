package com.jacknic.android.wanandroid.core.common

import android.content.Context
import androidx.annotation.Keep
import androidx.startup.Initializer
import timber.log.Timber

/**
 * 通用功能模块初始化
 *
 * @author Jacknic
 */
@Keep
class AppCommonInitializer : Initializer<Unit> {
    private val loggerTree = LoggerTree("WAN")
    private val log = TLog.create("AppCommonInitializer", BuildConfig.DEBUG)

    override fun create(context: Context) {
        Timber.plant(loggerTree)
        log.tag().d("create: AppCommonInitializer")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
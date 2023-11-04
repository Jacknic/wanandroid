package com.jacknic.android.wanandroid

import android.app.Application
import com.jacknic.android.wanandroid.core.common.TLog
import dagger.hilt.android.HiltAndroidApp

/**
 * 应用入口
 *
 * @author Jacknic
 */
@HiltAndroidApp
class App : Application() {
    private val log = TLog.create("App", BuildConfig.DEBUG)

    override fun onCreate() {
        super.onCreate()
        log.tag().d("onCreate: App created")
    }
}
package com.jacknic.wanandroid.util

import android.content.Context
import androidx.startup.Initializer
import com.jacknic.wanandroid.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * 初始化辅助类
 *
 * @author Jacknic
 */
class AppStartupInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(AndroidLogAdapter())
        }
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>>>()
}
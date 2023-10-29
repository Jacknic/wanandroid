package com.jacknic.android.wanandroid.core.common

import com.jacknic.android.core.common.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import timber.log.Timber

/**
 * 日志输出工具类
 *
 * @param tagPrefix tag便签前缀，便于筛选日志
 * @author Jack
 */
class LoggerTree(
    private val tagPrefix: String,
) : Timber.DebugTree() {
    private var hasFormatStrategy = false

    @Synchronized
    private fun ensureFormatStrategy() {
        if (!hasFormatStrategy) {
            val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(1)
                .showThreadInfo(true)
                .tag(tagPrefix)
                .methodOffset(4)
                .build()
            Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
            hasFormatStrategy = true
        }
    }

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        if (usePretty) {
            if (!hasFormatStrategy) {
                ensureFormatStrategy()
            }
            val trimStart = message.trim()
            if (trimStart.startsWith("{") || trimStart.startsWith("[")) {
                Logger.json(trimStart)
            } else {
                Logger.log(priority, tag, message, t)
            }
        } else {
            super.log(priority, "$tagPrefix-$tag", message, t)
        }
    }

    companion object {
        /**
         * 是否使用格式化打印
         */
        var usePretty: Boolean = BuildConfig.DEBUG
    }
}
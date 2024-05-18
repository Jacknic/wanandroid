package com.jacknic.android.wanandroid.core.common

import android.util.Log
import timber.log.Timber

/**
 * Timber log 工具类
 *
 * @author Jacknic
 */
class TLog @JvmOverloads constructor(
    private val tag: String,
    private val isLoggable: Boolean = false
) {

    /**
     * Log 拓展方法
     *
     * 判断是否输出及添加 tag
     */
    fun tag(): Timber.Tree {
        return if (isLoggable) {
            Timber.tag(tag)
        } else {
            LOG_TREE.tag(tag)
        }
    }

    /**
     * 日志等级定义
     */
    enum class LogLevel(val value: Int) {
        ALL(0),
        VERBOSE(Log.VERBOSE),
        DEBUG(Log.DEBUG),
        INFO(Log.INFO),
        WARN(Log.WARN),
        ERROR(Log.ERROR),
        ASSERT(Log.ASSERT)
    }

    companion object {

        /**
         * 默认Log输出实现
         */
        @JvmStatic
        private val LOG_TREE = object : Timber.Tree() {

            /**
             * 默认输出，需调用端设置 [Timber.plant] 输出的具体实现
             */
            private val tree = Timber.asTree()

            /**
             * 注入 tag 标签
             */
            fun tag(tag: String): Timber.Tree {
                Timber.tag(tag)
                return this
            }

            override fun isLoggable(tag: String?, priority: Int): Boolean {
                return priority >= logLevel.value
            }

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                tree.log(priority, t, message)
            }
        }

        /**
         * 全局日志输出等级
         */
        var logLevel = if (BuildConfig.DEBUG) LogLevel.VERBOSE else LogLevel.INFO

        /**
         * 创建日志输出对象
         *
         * @param tag 标签
         * @param isLoggable true 默认打开所有log，false 只打印 [logLevel] 及以上的log
         */
        @JvmStatic
        @JvmOverloads
        fun create(tag: String, isLoggable: Boolean = false) = TLog(tag, isLoggable)
    }
}
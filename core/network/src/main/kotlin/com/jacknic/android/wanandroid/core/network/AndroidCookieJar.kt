package com.jacknic.android.wanandroid.core.network

import android.util.Log
import android.webkit.CookieManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Cookie 同步存储
 *
 * @author Jacknic
 */
class AndroidCookieJar : CookieJar {

    private val cookieManager = CookieManager.getInstance()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.forEach {
            val cookieValue = it.toString()
            // Logger.d("存储：${url}->${cookieValue}")
            cookieManager.setCookie(url.toString(), cookieValue)
        }
        cookieManager.flush()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookieStringValue = cookieManager.getCookie(url.toString()).orEmpty()
        val valueList = cookieStringValue.split(";")
        // Logger.d("读取：${url}->${cookieStringValue}")
        return valueList.mapNotNull { Cookie.parse(url, it) }
    }

    companion object {

        @Volatile
        private var cookieJar: AndroidCookieJar? = null
        private const val TAG = "AndroidCookieJar"

        /**
         * 获取存储管理单例
         */
        fun get(): AndroidCookieJar {
            return cookieJar ?: synchronized(this) {
                AndroidCookieJar().also {
                    cookieJar = it
                }
            }
        }

        /**
         * 清除所有 Cookie
         */
        fun clear() {
            CookieManager.getInstance().removeAllCookies {
                Log.d(TAG, "clear: done=$it")
            }
        }
    }
}
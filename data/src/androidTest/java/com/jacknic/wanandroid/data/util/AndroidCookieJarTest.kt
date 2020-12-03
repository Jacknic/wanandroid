package com.jacknic.wanandroid.data.util

import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Jacknic
 */
class AndroidCookieJarTest {

    private lateinit var cookieJar: AndroidCookieJar
    private val rawCookies =
        "gitee-session-n=cGNaQzlHaDQ2SHQ5ckZ4VlYvRmcwemNLRkJKQ0hKd2lRUHdGZ054L2JkaEVVbU1sYjJ0czBoVC9HTWpaV0xuTnRvM3A3TW1mMlp1YlEvWUVrcEk2WFE9PS0tMy9WMmU3SzJNbHl2WjNjWFpCbGk3UT09--b62dab318a776e6c7e700dac9efe9528b84eeec7; domain=.gitee.com; path=/; HttpOnly"
    private val httpUrl = "https://gitee.com/a/b/c/d.html".toHttpUrlOrNull()!!

    @Before
    fun setUp() {
        cookieJar = AndroidCookieJar()
    }

    @Test
    fun saveFromResponse() {
        saveCookie()
    }

    private fun saveCookie() {
        val cookie = Cookie.parse(httpUrl, rawCookies)!!
        val list = listOf(cookie)
        cookieJar.saveFromResponse(httpUrl, list)
    }

    @Test
    fun loadForRequest() {
        saveCookie()
        val list = cookieJar.loadForRequest(httpUrl)
        Assert.assertFalse(list.isEmpty())
        list.forEach {
            println(it)
        }
    }
}
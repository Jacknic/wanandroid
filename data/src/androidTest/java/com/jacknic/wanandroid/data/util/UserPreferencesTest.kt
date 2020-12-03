package com.jacknic.wanandroid.data.util

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * 用户数据存储单元测试
 *
 * @author Jacknic
 */
class UserPreferencesTest {

    private lateinit var userPreferences: UserPreferences
    private val testUsername = "Jacknic"
    private val testPwd = "123456"

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        userPreferences = UserPreferences.getInstance(appContext)
    }

    @Test
    fun getUsername() {
        Assert.assertEquals("", userPreferences.username)
    }

    @Test
    fun setUsername() {
        userPreferences.username = testUsername
        Assert.assertEquals(testUsername, userPreferences.username)
    }

    @Test
    fun getPassword() {
    }

    @Test
    fun setPassword() {
    }

    @Test
    fun getLogged() {
    }

    @Test
    fun setLogged() {
    }

    @Test
    fun clear() {
        userPreferences.username = testUsername
        userPreferences.password = testPwd
        userPreferences.logged = true
        Assert.assertEquals(testUsername, userPreferences.username)
        Assert.assertEquals(testPwd, userPreferences.password)
        Assert.assertEquals(false, userPreferences.logged)
        userPreferences.clear()
        Assert.assertEquals("", userPreferences.username)
        Assert.assertEquals("", userPreferences.password)
        Assert.assertEquals(false, userPreferences.logged)

    }
}
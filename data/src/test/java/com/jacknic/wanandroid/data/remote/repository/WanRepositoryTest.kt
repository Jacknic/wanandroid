package com.jacknic.wanandroid.data.remote.repository

import com.jacknic.wanandroid.data.model.User
import com.jacknic.wanandroid.data.util.SafeResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class WanRepositoryTest {

    @Mock
    private lateinit var repo: WanRepository

    @Before
    fun setup() {

    }

    @Test
    fun whenLoginSuccess_should_hasUserInfo() = runBlocking {
        val username = "Jacknic"
        val user = User()
        user.nickname = username
        whenever(repo.login(any(), any())).thenReturn(SafeResult.Success(user))
        val result = repo.login(username, "123456")
        val success = result as SafeResult.Success<User>
        Assert.assertEquals(username, success.data.nickname)
    }
}
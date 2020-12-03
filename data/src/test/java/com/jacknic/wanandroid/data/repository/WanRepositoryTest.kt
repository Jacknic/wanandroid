package com.jacknic.wanandroid.data.repository

import com.jacknic.wanandroid.data.remote.repository.WanRepository
import com.jacknic.wanandroid.data.remote.source.WanDataSource
import com.jacknic.wanandroid.data.util.CloudServer
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * 玩Android数据访问单元测试
 *
 * @author Jacknic
 */
class WanRepositoryTest {

    private lateinit var wanRepository: WanRepository

    @Before
    fun setUp() {
        val wanApi = CloudServer(OkHttpClient()).getWanApi()
        val wanDataSource = WanDataSource(wanApi)
        wanRepository = WanRepository(wanDataSource)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun login() = runBlocking<Unit> {
    }

    @Test
    fun fetchBanner() = runBlocking<Unit> {
        wanRepository.fetchBanner().doSuccess {
            Assert.assertTrue(it.isNotEmpty())
        }.doError {
            Assert.fail(it.message)
        }
    }

    @Test
    fun fetchHomeList() = runBlocking<Unit> {
        wanRepository.fetchHomeList(0).doSuccess {
            Assert.assertEquals(20, it.datas.size)
        }.doError {
            Assert.fail(it.message)
        }
    }

    @Test
    fun fetchTopList() = runBlocking<Unit> {
        wanRepository.fetchTopList().doSuccess {
            Assert.assertTrue(it.isNotEmpty())
        }.doError {
            Assert.fail(it.message)
        }
    }

    @Test
    fun logout() = runBlocking<Unit> {
    }

    @Test
    fun fetchCollectData() = runBlocking<Unit> {
    }

    @Test
    fun fetchIntegral() = runBlocking<Unit> {
    }

    @Test
    fun collect() = runBlocking<Unit> {
    }

    @Test
    fun unCollect() = runBlocking<Unit> {
    }

    @Test
    fun fetchProjectTabList() = runBlocking<Unit> {
        wanRepository.fetchProjectTabList().doSuccess {
            Assert.assertTrue(it.isNotEmpty())
        }.doError {
            Assert.fail(it.message)
        }
    }

    @Test
    fun fetchAccountTabList() = runBlocking<Unit> {
        wanRepository.fetchAccountTabList().doSuccess {
            Assert.assertTrue(it.isNotEmpty())
        }.doError {
            Assert.fail(it.message)
        }
    }

    @Test
    fun fetchProjectList() = runBlocking<Unit> {

    }

    @Test
    fun listWeChatArticle() = runBlocking<Unit> {
    }

    @Test
    fun search() = runBlocking<Unit> {
    }

    @Test
    fun fetchSystemList() = runBlocking<Unit> {
    }

    @Test
    fun fetchSystemArticle() = runBlocking<Unit> {
    }

    @Test
    fun fetchNavigation() = runBlocking<Unit> {
    }

    @Test
    fun fetchRank() = runBlocking<Unit> {
    }

    @Test
    fun fetchIntegralRecord() = runBlocking<Unit> {
    }

    @Test
    fun fetchMyArticle() = runBlocking<Unit> {
    }

    @Test
    fun deleteMyArticle() = runBlocking<Unit> {
    }

    @Test
    fun shareArticle() = runBlocking<Unit> {
    }

    @Test
    fun register() = runBlocking<Unit> {
        wanRepository.register("aaaa", "bbbb", "cccc")
            .doError {
                // 信息不匹配
                println("")
            }.doSuccess {
                Assert.fail("测试注册接口有问题")
            }
    }
}
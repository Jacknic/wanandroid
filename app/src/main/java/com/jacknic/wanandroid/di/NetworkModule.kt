package com.jacknic.wanandroid.di

import com.jacknic.wanandroid.BuildConfig
import com.jacknic.wanandroid.data.remote.repository.AppUpdateRepository
import com.jacknic.wanandroid.data.remote.repository.WanRepository
import com.jacknic.wanandroid.data.remote.source.AppUpdateDataSource
import com.jacknic.wanandroid.data.remote.source.WanDataSource
import com.jacknic.wanandroid.data.util.AndroidCookieJar
import com.jacknic.wanandroid.data.util.CloudServer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * 网络请求依赖
 *
 * @author Jacknic
 */
@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.cookieJar(AndroidCookieJar())
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideCloudServer(okHttpClient: OkHttpClient) = CloudServer(okHttpClient)

    @Singleton
    @Provides
    fun provideWanRepository(cloudServer: CloudServer): WanRepository {
        val wanApi = cloudServer.getWanApi()
        val wanDataSource = WanDataSource(wanApi)
        return WanRepository(wanDataSource)
    }

    @Singleton
    @Provides
    fun provideUpdateRepository(cloudServer: CloudServer): AppUpdateRepository {
        val appUpdateApi = cloudServer.getUpdateApi()
        val appUpdateDataSource = AppUpdateDataSource(appUpdateApi)
        return AppUpdateRepository(appUpdateDataSource)
    }
}
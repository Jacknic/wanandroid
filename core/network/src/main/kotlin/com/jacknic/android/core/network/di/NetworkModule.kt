package com.jacknic.android.core.network.di

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import com.jacknic.android.core.common.BuildConfig
import com.jacknic.android.core.network.AndroidCookieJar
import com.jacknic.android.core.network.AppTrustManager
import com.jacknic.android.core.network.WanApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val URL_WAN_ANDROID = "https://wanandroid.com/"

    @Provides
    @Singleton
    fun wanApi(factory: Call.Factory): WanApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL_WAN_ANDROID)
            .callFactory(factory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(WanApi::class.java)
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory {
        val trustManager = AppTrustManager()
        val sslSocketFactory = Platform.get().newSslSocketFactory(trustManager)
        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
            )
            .cookieJar(AndroidCookieJar.get())
            .build()
    }

    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactory: Call.Factory,
        @ApplicationContext application: Context,
    ): ImageLoader = ImageLoader.Builder(application)
        .callFactory(okHttpCallFactory)
        .components { add(SvgDecoder.Factory()) }
        .respectCacheHeaders(false)
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger())
            }
        }
        .build()
}

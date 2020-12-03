package com.jacknic.wanandroid.data.util

import com.jacknic.wanandroid.data.remote.api.AppUpdateApi
import com.jacknic.wanandroid.data.remote.api.WanApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 服务端接口集合
 *
 * @author Jacknic
 */
class CloudServer(okHttpClient: OkHttpClient) {

    private var wanRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL_WAN_ANDROID)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * 玩安卓站点接口
     */
    fun getWanApi(): WanApi = wanRetrofit.create(WanApi::class.java)

    /**
     * 应用更新检测接口
     */
    fun getUpdateApi(): AppUpdateApi = wanRetrofit.create(AppUpdateApi::class.java)

    companion object {
        /**
         * 玩 Android 网址
         */
        const val URL_WAN_ANDROID = "https://wanandroid.com/"

    }
}
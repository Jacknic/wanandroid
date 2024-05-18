package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 玩安卓统一响应结构
 *
 * @author Jacknic
 */
data class WanResult<T>(
    /**
     * 数据
     */
    @SerializedName("data")
    val data: T? = null,

    /**
     * 响应码，0 为正常返回，其他异常
     */
    @SerializedName("errorCode")
    val errorCode: Int = 0,

    /**
     * 响应信息，错误码响应信息描述
     */
    @SerializedName("errorMsg")
    val errorMsg: String = ""
) {
    /**
     * 请求是否成功
     *
     * true 成功，false 失败
     */
    fun success() = ERROR_CODE_OK == errorCode

    companion object {

        /**
         * 请求成功
         */
        const val ERROR_CODE_OK = 0

        /**
         * 构建请求成功数据
         */
        @JvmStatic
        fun <T> success(data: T): WanResult<T> {
            return WanResult(data, ERROR_CODE_OK)
        }
    }
}
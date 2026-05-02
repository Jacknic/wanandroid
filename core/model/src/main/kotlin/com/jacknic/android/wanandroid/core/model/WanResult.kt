package com.jacknic.android.wanandroid.core.model

import com.google.gson.annotations.SerializedName

/**
 * 玩安卓统一响应结构
 *
 * 所有接口返回结构均遵循此格式：
 * - [errorCode] 为 0 表示请求成功，非0均为失败
 * - [errorMsg] 成功时为空字符串，失败时为错误描述
 * - [data] 为实际业务数据，类型因接口而异
 *
 * @param T 业务数据类型
 * @param data 业务数据
 * @param errorCode 响应码，0为成功，-1为通用错误，-1001为未登录
 * @param errorMsg 响应信息，错误码响应信息描述
 *
 * @author Jacknic
 */
data class WanResult<T>(
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("errorCode")
    val errorCode: Int = 0,
    @SerializedName("errorMsg")
    val errorMsg: String = ""
) {
    /**
     * 请求是否成功
     *
     * @return true 成功，false 失败
     */
    fun success() = ERROR_CODE_OK == errorCode

    companion object {

        /** 请求成功 */
        const val ERROR_CODE_OK = 0

        /** 通用错误（参数错误、操作失败等） */
        const val ERROR_CODE_ERROR = -1

        /** 未登录或登录已失效 */
        const val ERROR_CODE_UNAUTHORIZED = -1001

        /**
         * 构建请求成功数据
         */
        @JvmStatic
        fun <T> success(data: T): WanResult<T> {
            return WanResult(data, ERROR_CODE_OK)
        }
    }
}

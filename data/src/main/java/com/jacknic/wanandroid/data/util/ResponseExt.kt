package com.jacknic.wanandroid.data.util

import retrofit2.Response
import java.io.IOException

/**
 * 玩安卓响应数据封装
 */
typealias WanResponse<T> = Response<WanJson<T>>

/**
 * 响应结果转换判断
 **/
fun <I : Any, O : Any> Response<I>.safeResult(
    errorMsg: String,
    action: (data: I) -> SafeResult<O>?
): SafeResult<O> {
    return body()?.run(action) ?: SafeResult.Error(IOException(errorMsg))
}

/**
 * 响应结果简单判断
 **/
fun <T : Any> Response<T>.safeResult(
    errorMsg: String
): SafeResult<T> {
    return body()?.let { SafeResult.Success(it) } ?: SafeResult.Error(IOException(errorMsg))
}

/**
 * 判断玩Android数据格式请求
 */
fun <I : Any, O : Any> WanResponse<I>.wanResult(
    errorMsg: String,
    action: (data: I) -> SafeResult<O>?
) = safeResult(errorMsg) {
    if (it.errorCode == 0) {
        it.data?.run(action)
    } else {
        var msg = errorMsg
        if (it.errorMsg.isNotEmpty()) {
            msg = it.errorMsg
        }
        SafeResult.Error(IllegalAccessException(msg))
    }
}

/**
 * 解包玩Android数据
 */
fun <R : Any> WanResponse<R>.wanResult(errorMsg: String) =
    wanResult(errorMsg) { SafeResult.Success(it) }

package com.jacknic.android.wanandroid.core.network

import com.jacknic.android.wanandroid.core.model.WanResult
import retrofit2.HttpException

/**
 * 获取请求数据结果
 */
suspend fun <T> runResult(
    action: suspend () -> WanResult<T>
): Result<T> = runCatching {
    val result = try {
        action()
    } catch (e: Exception) {
        if (e is HttpException) {
            throw WanServerException(e.code(), e.message())
        } else {
            throw e
        }
    }

    val data = result.data
    return if (result.success() && data != null) {
        Result.success(data)
    } else {
        Result.failure(WanServerException(result.errorCode, result.errorMsg))
    }
}

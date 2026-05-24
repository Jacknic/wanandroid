package com.jacknic.android.wanandroid.core.network

import com.jacknic.android.wanandroid.core.model.WanResult
import retrofit2.HttpException

/**
 * 执行API请求并转换为 [Result]
 *
 * 将 [WanResult] 响应转换为 Kotlin [Result]，统一处理HTTP异常和业务错误：
 * - HTTP异常（如网络错误、404等）转换为 [WanServerException]
 * - 业务错误（errorCode非0）转换为 [WanServerException]
 * - 成功时返回 [Result.success]，data可能为null（如收藏/取消收藏等操作类接口）
 *
 * @param T 业务数据类型
 * @param action 挂起函数，执行API调用返回 [WanResult]
 * @return [Result] 包含业务数据或异常
 */
suspend fun <T> runResult(action: suspend () -> WanResult<T>): Result<T> = runCatching {
    val result =
        try {
            action()
        } catch (
            @Suppress("TooGenericExceptionCaught", "SwallowedException") e: Exception,
        ) {
            if (e is HttpException) {
                throw WanServerException(e.code(), e.message())
            } else {
                throw e
            }
        }

    return if (result.success()) {
        @Suppress("UNCHECKED_CAST")
        val data = result.data as T
        Result.success(data)
    } else {
        Result.failure(WanServerException(result.errorCode, result.errorMsg))
    }
}

package com.jacknic.android.core.network

import com.jacknic.android.core.model.WanResult
import retrofit2.Response

typealias WanResponse<T> = Response<WanResult<T>>

/**
 * 响应结果转换
 */
fun <T> WanResponse<T>.toResult(): Result<T> {
    val txzResult = body() ?: return Result.failure(
        WanServerException(code(), message())
    )
    return if (txzResult.success() && txzResult.data != null) {
        Result.success(txzResult.data!!)
    } else {
        Result.failure(WanServerException(txzResult.errorCode, txzResult.errorMsg))
    }
}

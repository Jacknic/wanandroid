package com.jacknic.android.wanandroid.core.common

/**
 * 通用状态数据密封类
 *
 * @author Jack
 */
sealed interface StateResult<out T> {
    /**
     * 加载中
     */
    object Loading : StateResult<Nothing>

    /**
     * 加载成功
     */
    data class Success<T>(val data: T) : StateResult<T>

    /**
     * 加载失败
     */
    data class Error(val exception: Throwable? = null) : StateResult<Nothing>

}

/**
 * 加载成功时执行
 */
inline fun <T> StateResult<T>.onSuccess(action: (value: T) -> Unit): StateResult<T> {
    if (this is StateResult.Success) {
        action(data)
    }
    return this
}

/**
 * 加载失败时执行
 */
inline fun <T> StateResult<T>.onError(action: (value: Throwable?) -> Unit): StateResult<T> {
    if (this is StateResult.Error) {
        action(exception)
    }
    return this
}

/**
 * 加载中执行
 */
inline fun <T> StateResult<T>.onLoading(action: () -> Unit): StateResult<T> {
    if (this is StateResult.Loading) {
        action()
    }
    return this
}

/**
 * 全状态判断逻辑
 *
 * @param loading 加载中
 * @param error 加载错误
 * @param success 加载成功
 */
inline fun <T> StateResult<T>.onState(
    loading: () -> Unit = {},
    error: (value: Throwable?) -> Unit = {},
    success: (value: T) -> Unit = {}
) {
    when (this) {
        StateResult.Loading -> loading()
        is StateResult.Error -> error(exception)
        is StateResult.Success -> success(data)
    }
}

/**
 * 数据类型转换
 *
 * @param transformer 转换函数
 */
inline fun <R, T> StateResult<T>.toStateResult(transformer: (T) -> R): StateResult<R> {
    return when (this) {
        is StateResult.Error -> this
        StateResult.Loading -> StateResult.Loading
        is StateResult.Success -> StateResult.Success(transformer(data))
    }
}

/**
 * 转换为带状态结构
 */
fun <T> Result<T>.toStateResult(): StateResult<T> {
    return fold(onSuccess = { StateResult.Success(it) }, onFailure = { StateResult.Error(it) })
}

/**
 * 转换为带状态结构
 *
 * @param transformer 数据类型转换
 */
inline fun <R, T> Result<T>.toStateResult(transformer: (T) -> R): StateResult<R> {
    return when (val result = toStateResult()) {
        is StateResult.Error -> result
        StateResult.Loading -> StateResult.Loading
        is StateResult.Success -> StateResult.Success(transformer(result.data))
    }
}
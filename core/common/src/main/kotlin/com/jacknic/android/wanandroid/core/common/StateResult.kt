package com.jacknic.android.wanandroid.core.common

import com.jacknic.android.wanandroid.core.common.StateResult.Loading
import com.jacknic.android.wanandroid.core.common.StateResult.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * 通用状态数据密封类
 *
 * @author Jacknic
 */
sealed interface StateResult<out T> {
    /**
     * 加载中
     */
    data object Loading : StateResult<Nothing>

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
 * 获取数据或返回null对象
 */
fun <T> StateResult<T>.getDataOrNull(): T? {
    return if (this is StateResult.Success) data else null
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

/**
 * 是否加载中
 */
fun <T> StateResult<T>?.loading() = this is Loading

/**
 * 是否加载成功
 */
fun <T> StateResult<T>?.success() = this is Success

/**
 * 是否加载失败
 */
fun <T> StateResult<T>?.error() = this is StateResult.Error

/**
 * 带加载状态数据流转换
 */
suspend fun <T> MutableStateFlow<StateResult<T>?>.withLoading(
    loading: Boolean = true,
    action: suspend (StateResult<T>?) -> StateResult<T>
) {
    if (loading) {
        update { Loading }
    }
    update {
        try {
            action(it)
        } catch (e: Exception) {
            StateResult.Error(e)
        }
    }
}
package com.jacknic.android.wanandroid.core.network

import com.jacknic.android.wanandroid.core.model.WanResult

/**
 * 玩安卓服务异常
 *
 * @property code 错误码
 * @property msg 错误信息
 *
 * @author Jacknic
 */
class WanServerException(val code: Int, val msg: String = "") : Exception(msg) {
    fun isUnauthorized() = code == WanResult.ERROR_CODE_UNAUTHORIZED
    fun isError() = code == WanResult.ERROR_CODE_ERROR
}

/**
 * 判断异常是否为未登录
 *
 * @return true 是，false 否
 */
fun Throwable?.isUnauthorized() = this is WanServerException && this.isUnauthorized()

/**
 * 判断异常是否为错误
 *
 * @return true 是，false 否
 */
fun Throwable?.isError() = this is WanServerException && this.isError()

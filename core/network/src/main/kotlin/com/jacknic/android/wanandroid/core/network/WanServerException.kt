package com.jacknic.android.wanandroid.core.network

/**
 * 玩安卓服务异常
 *
 * @property code 错误码
 * @property msg 错误信息
 *
 * @author Jacknic
 */
class WanServerException(val code: Int, val msg: String = "") : Exception(msg)

package com.jacknic.wanandroid.data.util

import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException

/**
 * 请求异常统一处理
 */
suspend fun <T : Any> safeApiCall(
    call: suspend () -> SafeResult<T>
): SafeResult<T> {
    return try {
        call()
    } catch (e: Exception) {
        e.printStackTrace()
        val msg = when (e) {
            is HttpException -> "请求响应错误！"
            is ConnectException -> "连接服务器失败！"
            is SocketTimeoutException -> "连接超时，请稍后重试！"
            is SSLHandshakeException -> "安全握手连接错误！"
            is JSONException, is JsonParseException, is ParseException -> "数据解析错误！"
            else -> "网络错误，请检查网络！"
        }
        SafeResult.Error(IOException(msg, e))
    }
}

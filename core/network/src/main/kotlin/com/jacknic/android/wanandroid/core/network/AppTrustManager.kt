package com.jacknic.android.wanandroid.core.network

import android.annotation.SuppressLint
import okhttp3.internal.platform.Platform
import java.security.cert.CertificateNotYetValidException
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

/**
 * 证书验证管理器
 *
 * @author Jacknic
 */
@SuppressLint("CustomX509TrustManager")
class AppTrustManager : X509TrustManager {
    private val trustManager = Platform.get().platformTrustManager()

    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        trustManager.checkClientTrusted(chain, authType)
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        try {
            trustManager.checkServerTrusted(chain, authType)
        } catch (e: Exception) {
            val cause = e.cause
            // 忽略有效期验证异常
            if (cause is CertificateNotYetValidException) {
                cause.printStackTrace()
            } else {
                throw e
            }
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return trustManager.acceptedIssuers
    }
}
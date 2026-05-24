package com.jacknic.android.wanandroid.core.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 安全凭据数据源 - 使用 EncryptedSharedPreferences 加密存储用户凭据
 *
 * 通过 Android Keystore + AES 加密保护敏感信息（用户名、密码），
 * 防止在设备被 root 或数据被提取时凭据泄露。
 */
@Singleton
class SecureCredentialsDataSource @Inject constructor(@param:ApplicationContext private val context: Context) {
    private val masterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val encryptedPrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    /**
     * 保存用户凭据
     *
     * @param username 用户名
     * @param password 密码
     * @param rememberPassword 是否记住密码
     */
    fun saveCredentials(username: String, password: String, rememberPassword: Boolean) {
        encryptedPrefs.edit()
            .putString(KEY_USERNAME, username)
            .putString(KEY_PASSWORD, password)
            .putBoolean(KEY_REMEMBER_PASSWORD, rememberPassword)
            .apply()
    }

    /**
     * 获取已保存的用户名
     */
    fun getSavedUsername(): String? = encryptedPrefs.getString(KEY_USERNAME, null)

    /**
     * 获取已保存的密码
     */
    fun getSavedPassword(): String? = encryptedPrefs.getString(KEY_PASSWORD, null)

    /**
     * 是否记住密码
     */
    fun isRememberPassword(): Boolean = encryptedPrefs.getBoolean(KEY_REMEMBER_PASSWORD, false)

    /**
     * 清除所有已保存的凭据
     */
    fun clearCredentials() {
        encryptedPrefs.edit().clear().apply()
    }

    /**
     * 是否存在已保存的凭据
     */
    fun hasCredentials(): Boolean {
        val username = getSavedUsername()
        val password = getSavedPassword()
        return !username.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    companion object {
        private const val FILE_NAME = "secure_credentials"
        private const val KEY_USERNAME = "saved_username"
        private const val KEY_PASSWORD = "saved_password"
        private const val KEY_REMEMBER_PASSWORD = "remember_password"
    }
}

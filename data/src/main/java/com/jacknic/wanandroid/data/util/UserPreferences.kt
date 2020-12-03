package com.jacknic.wanandroid.data.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * 用户偏好设置存储
 */
class UserPreferences(private val prefer: SharedPreferences) {

    private val keyUsername = "username"
    private val keyPassword = "password"
    private val keyLogged = "logged"

    var username: String
        get() = prefer.getString(keyUsername, "").orEmpty()
        set(value) = prefer.edit { putString(keyUsername, value) }

    var password: String
        get() = prefer.getString(keyPassword, "").orEmpty()
        set(value) = prefer.edit { putString(keyPassword, value) }

    var logged: Boolean
        get() = prefer.getBoolean(keyLogged, false)
        set(value) = prefer.edit { putBoolean(keyLogged, value) }

    /**
     * 清空数据
     */
    fun clear() = prefer.edit().clear().apply()

    companion object {

        private const val PREFER_NAME = "user_prefer"

        fun getInstance(context: Context): UserPreferences {
            val sharedPreferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
            return UserPreferences(sharedPreferences)
        }
    }
}
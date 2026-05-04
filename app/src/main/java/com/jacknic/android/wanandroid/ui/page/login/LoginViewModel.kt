package com.jacknic.android.wanandroid.ui.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.common.withLoading
import com.jacknic.android.wanandroid.core.data.UserDataRepository
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: WanRepository,
    private val userDataRepo: UserDataRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<StateResult<UserInfo>?>(null)
    val userInfo = _userInfo.asStateFlow()

    private val _registerResult = MutableStateFlow<StateResult<UserInfo>?>(null)
    val registerResult = _registerResult.asStateFlow()

    private var loginJob: Job? = null
    private var registerJob: Job? = null

    fun login(username: String, password: String) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            _userInfo.withLoading {
                repo.login(username, password).toStateResult()
            }
        }
    }

    fun register(username: String, password: String, repassword: String) {
        registerJob?.cancel()
        registerJob = viewModelScope.launch {
            _registerResult.withLoading {
                repo.register(username, password, repassword).toStateResult()
            }
        }
    }

    /**
     * 跳过登录
     */
    fun setSkipLogin(skipLogin: Boolean) {
        viewModelScope.launch {
            userDataRepo.setSkipLogin(skipLogin)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userDataRepo.setSkipLogin(false)
            repo.logout()
        }
    }

    fun resetRegisterResult() {
        _registerResult.value = null
    }

    fun resetLoginResult() {
        _userInfo.value = null
    }

    companion object {
        /** 密码强度：弱 */
        const val PASSWORD_STRENGTH_WEAK = 0
        /** 密码强度：中 */
        const val PASSWORD_STRENGTH_MEDIUM = 1
        /** 密码强度：强 */
        const val PASSWORD_STRENGTH_STRONG = 2

        /**
         * 评估密码强度
         * 弱：纯数字或纯字母，长度 < 6
         * 中：包含数字和字母，或长度 >= 6
         * 强：包含数字、字母和特殊字符，长度 >= 8
         */
        fun evaluatePasswordStrength(password: String): Int {
            if (password.length < 6) return PASSWORD_STRENGTH_WEAK
            val hasDigit = password.any { it.isDigit() }
            val hasLetter = password.any { it.isLetter() }
            val hasSpecial = password.any { !it.isLetterOrDigit() }
            return when {
                hasDigit && hasLetter && hasSpecial && password.length >= 8 -> PASSWORD_STRENGTH_STRONG
                hasDigit && hasLetter -> PASSWORD_STRENGTH_MEDIUM
                else -> PASSWORD_STRENGTH_WEAK
            }
        }

        /**
         * 校验用户名格式
         * 规则：3-20位，仅支持字母、数字和下划线
         */
        fun validateUsername(username: String): UsernameError? {
            if (username.isBlank()) return UsernameError.EMPTY
            if (username.length < 3) return UsernameError.TOO_SHORT
            if (username.length > 20) return UsernameError.TOO_LONG
            if (!username.all { it.isLetterOrDigit() || it == '_' }) return UsernameError.INVALID_CHARS
            return null
        }

        /**
         * 校验密码格式
         * 规则：6-20位
         */
        fun validatePassword(password: String): PasswordError? {
            if (password.isBlank()) return PasswordError.EMPTY
            if (password.length < 6) return PasswordError.TOO_SHORT
            if (password.length > 20) return PasswordError.TOO_LONG
            return null
        }

        /**
         * 校验确认密码
         */
        fun validateConfirmPassword(password: String, confirmPassword: String): ConfirmPasswordError? {
            if (confirmPassword.isBlank()) return ConfirmPasswordError.EMPTY
            if (password != confirmPassword) return ConfirmPasswordError.MISMATCH
            return null
        }
    }
}

/** 用户名校验错误 */
enum class UsernameError(val message: String) {
    EMPTY("用户名不能为空"),
    TOO_SHORT("用户名至少3个字符"),
    TOO_LONG("用户名最多20个字符"),
    INVALID_CHARS("用户名仅支持字母、数字和下划线")
}

/** 密码校验错误 */
enum class PasswordError(val message: String) {
    EMPTY("密码不能为空"),
    TOO_SHORT("密码至少6个字符"),
    TOO_LONG("密码最多20个字符")
}

/** 确认密码校验错误 */
enum class ConfirmPasswordError(val message: String) {
    EMPTY("请再次输入密码"),
    MISMATCH("两次输入的密码不一致")
}

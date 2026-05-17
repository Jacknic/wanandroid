package com.jacknic.android.wanandroid.ui.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.common.withLoading
import com.jacknic.android.wanandroid.core.data.UserDataRepository
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.UserInfo
import com.jacknic.android.wanandroid.ui.component.CollectStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: WanRepository,
    private val userDataRepo: UserDataRepository,
    private val collectStateManager: CollectStateManager
) : ViewModel() {

    private val _userInfo = MutableStateFlow<StateResult<UserInfo>?>(null)
    val userInfo = _userInfo.asStateFlow()

    private val _registerResult = MutableStateFlow<StateResult<UserInfo>?>(null)
    val registerResult = _registerResult.asStateFlow()

    /** 已保存的用户名 */
    private val _savedUsername = MutableStateFlow("")
    val savedUsername = _savedUsername.asStateFlow()

    /** 已保存的密码 */
    private val _savedPassword = MutableStateFlow("")
    val savedPassword = _savedPassword.asStateFlow()

    /** 是否记住密码 */
    private val _rememberPassword = MutableStateFlow(false)
    val rememberPassword = _rememberPassword.asStateFlow()

    /** 是否存在已保存的凭据 */
    private val _hasCredentials = MutableStateFlow(false)
    val hasCredentials = _hasCredentials.asStateFlow()

    private var loginJob: Job? = null
    private var registerJob: Job? = null

    init {
        loadSavedCredentials()
    }

    /**
     * 从安全存储加载已保存的凭据
     */
    private fun loadSavedCredentials() {
        _savedUsername.value = userDataRepo.getSavedUsername() ?: ""
        _savedPassword.value = userDataRepo.getSavedPassword() ?: ""
        _rememberPassword.value = userDataRepo.isRememberPassword()
        _hasCredentials.value = userDataRepo.hasCredentials()
    }

    fun login(username: String, password: String) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            _userInfo.withLoading {
                repo.login(username, password).toStateResult().also { result ->
                    if (result is StateResult.Success) {
                        collectStateManager.initFromUserInfo(result.data.collectIds)
                    }
                }
            }
        }
    }

    /**
     * 登录并保存凭据（如果记住密码被勾选）
     */
    fun loginWithCredentials(username: String, password: String, rememberPassword: Boolean) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            _userInfo.withLoading {
                repo.login(username, password).toStateResult().also { result ->
                    if (result is StateResult.Success) {
                        collectStateManager.initFromUserInfo(result.data.collectIds)
                        // 登录成功后保存凭据
                        if (rememberPassword) {
                            userDataRepo.saveCredentials(username, password, rememberPassword)
                            _rememberPassword.value = true
                            _hasCredentials.value = true
                        } else {
                            // 未勾选记住密码则清除已保存的凭据
                            userDataRepo.clearCredentials()
                            _rememberPassword.value = false
                            _hasCredentials.value = false
                        }
                    }
                }
            }
        }
    }

    fun register(username: String, password: String, repassword: String) {
        registerJob?.cancel()
        registerJob = viewModelScope.launch {
            _registerResult.withLoading {
                repo.register(username, password, repassword).toStateResult().also { result ->
                    if (result is StateResult.Success) {
                        collectStateManager.initFromUserInfo(result.data.collectIds)
                    }
                }
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
            collectStateManager.clear()
            // 登出时清除凭据
            if (userDataRepo.isRememberPassword()) {
                userDataRepo.clearCredentials()
            }
        }
    }

    /**
     * 清除已保存的凭据
     */
    fun clearSavedCredentials() {
        viewModelScope.launch {
            userDataRepo.clearCredentials()
            _savedUsername.value = ""
            _savedPassword.value = ""
            _rememberPassword.value = false
            _hasCredentials.value = false
        }
    }

    /**
     * 更新记住密码选项
     */
    fun updateRememberPassword(remember: Boolean) {
        _rememberPassword.value = remember
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

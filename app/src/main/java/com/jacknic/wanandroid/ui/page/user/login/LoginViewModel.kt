package com.jacknic.wanandroid.ui.page.user.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.wanandroid.data.remote.repository.WanRepository
import com.jacknic.wanandroid.data.util.UserPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class LoginViewModel @ViewModelInject constructor(
    private val wanRepository: WanRepository,
    private val userPreferences: UserPreferences,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var loginJob: Job? = null
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginState = MutableLiveData<LoginState>()

    data class LoginState(val loading: Boolean = false, val error: Throwable? = null)

    /**
     * 执行登录
     */
    fun login() {
        loginJob?.cancel()
        val usernameValue = username.value
        if (usernameValue.isNullOrEmpty()) {
            loginFail("用户名不能为空")
            return
        }
        val passwordValue = password.value
        if (passwordValue.isNullOrEmpty()) {
            loginFail("密码不能为空")
            return
        }
        loginJob = viewModelScope.launch {
            postLoginState(true)
            val result = wanRepository.login(usernameValue, passwordValue)
            result.doSuccess {
                userPreferences.username = usernameValue
                userPreferences.password = passwordValue
                userPreferences.logged = true
                postLoginState()
            }.doError {
                postLoginState(error = it)
            }
        }
    }

    /**
     * 上报登录请求状态
     */
    private fun postLoginState(loading: Boolean = false, error: Throwable? = null) {
        loginState.postValue(LoginState(loading, error))
    }

    /**
     * 输入信息错误
     */
    private fun loginFail(message: String) {
        postLoginState(error = IllegalArgumentException(message))
    }

    fun cancelLogin() {
        loginJob?.cancel()
    }
}
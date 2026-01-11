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

    private var loginJob: Job? = null

    fun login(username: String, password: String) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            _userInfo.withLoading {
                repo.login(username, password).toStateResult()
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
}
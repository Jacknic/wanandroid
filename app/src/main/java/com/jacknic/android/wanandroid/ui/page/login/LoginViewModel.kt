package com.jacknic.android.wanandroid.ui.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: WanRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<StateResult<UserInfo>?>(null)

    val userInfo = _userInfo.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _userInfo.value = StateResult.Loading
            _userInfo.value = repo.login(username, password).toStateResult()
        }
    }
}
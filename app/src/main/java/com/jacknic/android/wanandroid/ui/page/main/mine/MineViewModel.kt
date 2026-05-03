package com.jacknic.android.wanandroid.ui.page.main.mine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.common.withLoading
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.CoinInfo
import com.jacknic.android.wanandroid.core.model.PersonalInfo
import com.jacknic.android.wanandroid.core.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MineViewModel @Inject constructor(
    private val repo: WanRepository
) : ViewModel() {

    private val _personalInfo = MutableStateFlow<StateResult<PersonalInfo>?>(null)
    val personalInfo = _personalInfo.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            _personalInfo.withLoading {
                repo.getUserLgUserinfo().toStateResult()
            }
        }
    }

    fun refresh() = loadUserInfo()
}

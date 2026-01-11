package com.jacknic.android.wanandroid.ui.page.splash

import androidx.lifecycle.ViewModel
import com.jacknic.android.wanandroid.core.data.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(userDataRepo: UserDataRepository) : ViewModel() {
    val skipLogin = userDataRepo.skipLoginFlow()
}
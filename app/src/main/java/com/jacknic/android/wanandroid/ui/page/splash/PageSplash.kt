package com.jacknic.android.wanandroid.ui.page.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.navTop
import com.jacknic.android.wanandroid.ui.page.toMain

@Composable
fun PageSplash(nav: NavHostController, vm: SplashViewModel = hiltViewModel()) {
    val skipLogin by vm.skipLogin.collectAsStateWithLifecycle(null)

    LaunchedEffect(skipLogin) {
        if (skipLogin == true) {
            nav.toMain()
        } else if (skipLogin == false) {
            nav.navTop(Page.Login, Page.Splash)
        }
    }
}
package com.jacknic.android.wanandroid.ui.page.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.navTop
import com.jacknic.android.wanandroid.ui.page.toMain

@Composable
fun PageSplash(nav: NavHostController, vm: SplashViewModel = hiltViewModel()) {
    val skipLogin by vm.skipLogin.collectAsStateWithLifecycle(null)

    Box(Modifier.background(MaterialTheme.colorScheme.primary)) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(com.jacknic.android.wanandroid.core.ui.R.drawable.ic_launcher_foreground),
                modifier = Modifier.size(200.dp),
                contentDescription = ""
            )
        }
    }
    LaunchedEffect(skipLogin) {
        if (skipLogin == true) {
            nav.toMain()
        } else if (skipLogin == false) {
            nav.navTop(Page.Login)
        }
    }
}
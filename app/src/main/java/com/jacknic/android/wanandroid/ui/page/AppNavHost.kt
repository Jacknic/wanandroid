package com.jacknic.android.wanandroid.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jacknic.android.wanandroid.ui.page.browser.PageBrowser
import com.jacknic.android.wanandroid.ui.page.login.PageLogin
import com.jacknic.android.wanandroid.ui.page.main.PageMain
import com.jacknic.android.wanandroid.ui.page.search.PageSearch
import com.jacknic.android.wanandroid.ui.page.setting.PageSetting
import com.jacknic.android.wanandroid.ui.page.splash.PageSplash

object Page {

    /**
     * 主页面
     */
    const val Main = "PageMain"

    const val Splash = "PageSplash"

    /**
     * 搜索页面
     */
    const val Search = "PageSearch"
    const val Browser = "PageBrowser"
    const val Setting = "PageSetting"
    const val Login = "PageLogin"

}

/**
 * 置顶并清空页面
 */
fun NavHostController.navTop(page: String) {
    navigate(page) {
        popUpTo(page) {
            inclusive = false
        }
    }
}

/**
 * 跳转到首页
 */
fun NavHostController.toMain() = navTop(Page.Main)

val LocalNavCtrl = compositionLocalOf<NavHostController>(structuralEqualityPolicy()) {
    throw IllegalAccessException("未初始化导航组件")
}

/**
 * 页面导航图
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    nav: NavHostController = rememberNavController(),
    startDestination: String = Page.Splash
) {
    CompositionLocalProvider(LocalNavCtrl provides nav) {
        NavHost(
            modifier = modifier,
            navController = nav,
            startDestination = startDestination
        ) {
            composable(Page.Splash) { PageSplash(nav) }
            composable(Page.Main) { PageMain() }
            composable(Page.Search) { PageSearch() }
            composable(Page.Browser) { PageBrowser() }
            composable(Page.Setting) { PageSetting() }
            composable(Page.Login) { PageLogin(nav) }
        }
    }
}

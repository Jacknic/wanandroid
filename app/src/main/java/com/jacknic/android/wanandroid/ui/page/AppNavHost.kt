@file:Suppress("MatchingDeclarationName")

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
import com.jacknic.android.wanandroid.ui.component.CollectStateManager
import com.jacknic.android.wanandroid.ui.component.ReadingHistoryManager
import com.jacknic.android.wanandroid.ui.page.browser.PageBrowser
import com.jacknic.android.wanandroid.ui.page.browser.openBrowser
import com.jacknic.android.wanandroid.ui.page.collection.PageCollection
import com.jacknic.android.wanandroid.ui.page.login.PageLogin
import com.jacknic.android.wanandroid.ui.page.main.PageMain
import com.jacknic.android.wanandroid.ui.page.readinghistory.PageReadingHistory
import com.jacknic.android.wanandroid.ui.page.search.PageSearch
import com.jacknic.android.wanandroid.ui.page.setting.PageSetting
import com.jacknic.android.wanandroid.ui.page.splash.PageSplash
import com.jacknic.android.wanandroid.ui.page.todo.PageTodo

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
    const val Collection = "PageCollection"
    const val ReadingHistory = "PageReadingHistory"
    const val Todo = "PageTodo"
}

/**
 * 置顶并清空页面
 */
fun NavHostController.navTop(page: String, pagePop: String) {
    navigate(page) {
        popUpTo(pagePop) {
            inclusive = true
        }
    }
}

/**
 * 跳转到首页
 */
fun NavHostController.toMain() = navTop(Page.Main, Page.Splash)

/**
 * 打开浏览器页面
 */
fun NavHostController.openBrowser(url: String) = openBrowser(this, url)

val LocalNavCtrl = compositionLocalOf<NavHostController>(structuralEqualityPolicy()) {
    throw IllegalAccessException("未初始化导航组件")
}

/**
 * 收藏状态管理器 CompositionLocal
 */
val LocalCollectStateManager = compositionLocalOf<CollectStateManager> {
    throw IllegalAccessException("未初始化收藏状态管理器")
}

/**
 * 阅读记录管理器 CompositionLocal
 */
val LocalReadingHistoryManager = compositionLocalOf<ReadingHistoryManager> {
    throw IllegalAccessException("未初始化阅读记录管理器")
}

/**
 * 页面导航图
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    nav: NavHostController = rememberNavController(),
    startDestination: String = Page.Splash,
    collectStateManager: CollectStateManager,
    readingHistoryManager: ReadingHistoryManager,
) {
    CompositionLocalProvider(
        LocalNavCtrl provides nav,
        LocalCollectStateManager provides collectStateManager,
        LocalReadingHistoryManager provides readingHistoryManager,
    ) {
        NavHost(
            modifier = modifier,
            navController = nav,
            startDestination = startDestination,
        ) {
            composable(Page.Splash) { PageSplash(nav) }
            composable(Page.Main) { PageMain() }
            composable(Page.Search) { PageSearch() }
            composable(Page.Browser) { PageBrowser() }
            composable(Page.Setting) { PageSetting() }
            composable(Page.Login) { PageLogin(nav) }
            composable(Page.Collection) { PageCollection() }
            composable(Page.ReadingHistory) { PageReadingHistory() }
            composable(Page.Todo) { PageTodo() }
        }
    }
}

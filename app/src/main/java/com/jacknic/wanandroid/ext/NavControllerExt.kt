package com.jacknic.wanandroid.ext

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.jacknic.wanandroid.MainNavDirections
import com.jacknic.wanandroid.R

val defaultNavOptions = navOptions {
    anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
    }
}

/**
 * 跳转浏览器
 **/
fun NavController.toBrowser(url: String, title: String = "") {
    val directions = MainNavDirections.navToBrowserPage(url, title)
    navigate(directions)
}

/**
 * 页面跳转
 **/
fun NavController.toPage(@IdRes pageId: Int, args: Bundle? = null) {
    navigate(pageId, args, defaultNavOptions)
}

/**
 * 置顶页面
 */
fun NavController.topPage(@IdRes topPageId: Int) {
    navigate(topPageId, null, navOptions {
        popUpTo(R.id.mainNav) { inclusive = false }
    })
}
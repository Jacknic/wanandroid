package com.jacknic.wanandroid.ext

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * 显示为对话框
 */
fun DialogFragment.showDialog(fragmentManager: FragmentManager, tag: String? = null) {
    if (!isAdded) {
        val realTag = tag ?: this.javaClass.name
        showNow(fragmentManager, realTag)
    }
}

/**
 *  隐藏输入法
 */
fun View.hideInput() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * [ViewPager] 与 [BottomNavigationView] 联动
 */
fun ViewPager.setupWithBottomNav(bottomNav: BottomNavigationView, smoothScroll: Boolean) {
    addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            if (position < bottomNav.menu.size) {
                val menuItem = bottomNav.menu[position]
                if (bottomNav.selectedItemId != menuItem.itemId) {
                    bottomNav.selectedItemId = menuItem.itemId
                }
            }
        }
    })
    bottomNav.setOnNavigationItemSelectedListener {
        bottomNav.menu.forEachIndexed { index, item ->
            if (item == it) {
                setCurrentItem(index, smoothScroll)
                return@setOnNavigationItemSelectedListener true
            }
        }
        false
    }
}
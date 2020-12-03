package com.jacknic.wanandroid.ui.page.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jacknic.wanandroid.databinding.PageMainBinding
import com.jacknic.wanandroid.ext.setupWithBottomNav
import com.jacknic.wanandroid.ui.base.AbstractFragment
import com.jacknic.wanandroid.ui.page.category.CategoryPage
import com.jacknic.wanandroid.ui.page.main.home.HomeFrag
import com.jacknic.wanandroid.ui.page.main.mine.MineFrag
import com.jacknic.wanandroid.ui.page.user.login.LoginPage

/**
 * 主界面
 *
 * @author Jacknic
 */
class MainPage : AbstractFragment<PageMainBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pageList = listOf<Fragment>(
            LoginPage(),
            HomeFrag(),
            CategoryPage(),
            HomeFrag(),
            MineFrag()
        )
        val adapter = object : FragmentStatePagerAdapter(
            childFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                return pageList[position]
            }

            override fun getCount(): Int {
                return pageList.size
            }
        }
        bind.vpMain.adapter = adapter
        bind.vpMain.setupWithBottomNav(bind.bnvMain, false)
    }
}
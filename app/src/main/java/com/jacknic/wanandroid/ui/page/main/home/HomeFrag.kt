package com.jacknic.wanandroid.ui.page.main.home

import android.os.Bundle
import android.view.View
import com.jacknic.wanandroid.R
import com.jacknic.wanandroid.databinding.FragHomeBinding
import com.jacknic.wanandroid.ui.base.BaseFragment

/**
 * 首页内容
 *
 * @author Jacknic
 */
class HomeFrag : BaseFragment<FragHomeBinding>() {

    override val layoutResId = R.layout.frag_home
    override val menuResId = R.menu.menu_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.vpHome.adapter = HomeSubFragAdapter(requireContext(), childFragmentManager)
        bind.titleTabs.setupWithViewPager(bind.vpHome)
    }
}
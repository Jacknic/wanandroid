package com.jacknic.wanandroid.ui.page.main.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jacknic.wanandroid.R

/**
 * @author Jacknic
 */
class HomeSubFragAdapter(context: Context, private val fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val subFragTitleList = context.resources.getStringArray(R.array.home_tab)
    private val cacheFragMap = mutableMapOf<String, Fragment>()
    private val subFragClassList = arrayOf(
        HeadlineListFrag::class.java,
        ArticleListFrag::class.java,
        ArticleListFrag::class.java,
        ArticleListFrag::class.java,
        ArticleListFrag::class.java
    )

    override fun getItem(position: Int): Fragment {
        val subFragClass = subFragClassList[position]
        val fragTag = subFragClass.name + ":" + position
        return cacheFragMap[fragTag] ?: subFragClass.newInstance().also {
            cacheFragMap[fragTag] = it
        }
    }

    override fun getCount() = subFragClassList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return subFragTitleList[position]
    }
}
package com.jacknic.wanandroid.ui.page.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jacknic.wanandroid.R
import com.jacknic.wanandroid.databinding.FragArticleListBinding
import com.jacknic.wanandroid.ui.base.BaseFragment

/**
 * 文章列表页面
 *
 * @author Jacknic
 */
class ArticleListFrag : BaseFragment<FragArticleListBinding>() {

    override val layoutResId = R.layout.frag_article_list
    private val articleListAdapter by lazy { ArticleAdapter() }
    private val vm by viewModels<ArticleListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
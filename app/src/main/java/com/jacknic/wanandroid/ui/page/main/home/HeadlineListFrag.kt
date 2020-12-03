package com.jacknic.wanandroid.ui.page.main.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.jacknic.wanandroid.R
import com.jacknic.wanandroid.databinding.FragHeadlineListBinding
import com.jacknic.wanandroid.ext.toBrowser
import com.jacknic.wanandroid.ui.base.BaseFragment
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 文章列表页面
 *
 * @author Jacknic
 */
@AndroidEntryPoint
class HeadlineListFrag : BaseFragment<FragHeadlineListBinding>() {

    override val layoutResId = R.layout.frag_headline_list
    private val articleListAdapter by lazy { ArticleAdapter() }
    private val vm: ArticleListViewModel by activityViewModels()

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.banner.apply {
            val imgList = listOf(
                "https://www.wanandroid.com/blogimgs/dae161d5-fd4e-4c7e-8d53-b2f41bc3100e.png",
                "https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png",
                "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
                "https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png"
            )
            adapter = object : BannerAdapter<String, RecyclerView.ViewHolder>(imgList) {
                override fun onCreateHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    val imageView = ImageView(parent.context)
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    imageView.setBackgroundResource(R.color.green)
                    imageView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    return object : RecyclerView.ViewHolder(imageView) {}
                }

                override fun onBindView(
                    holder: RecyclerView.ViewHolder,
                    data: String,
                    position: Int,
                    size: Int
                ) {
                    with(holder.itemView) {
                        if (this is ImageView) {
                            Glide.with(this).load(data).into(this)
                        }
                    }
                }
            }
            indicator = CircleIndicator(context)
        }

        articleListAdapter.onClickListener = { _, article ->
            navCtrl.toBrowser(article.link, article.title)
        }
        bind.swipeRefresh.setOnRefreshListener {
            articleListAdapter.refresh()
        }

        bind.swipeRefresh.setOnChildScrollUpCallback { _, _ -> canScrollUp }
        bind.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            canScrollUp = verticalOffset != 0
        })

        articleListAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Error -> {
                    Toast.makeText(requireContext(), "加载数据出错", Toast.LENGTH_SHORT).show()
                    bind.swipeRefresh.isRefreshing = false
                }
                is LoadState.Loading -> {
                    bind.swipeRefresh.isRefreshing = true
                }
                is LoadState.NotLoading -> {
                    bind.swipeRefresh.isRefreshing = false
                }
            }

        }
        bind.rvArticleList.apply {
            adapter = articleListAdapter.withLoadStateHeader(ArticleLoadStateHeadAdapter())
            val dividerItemDecoration =
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            val drawable =
                ResourcesCompat.getDrawable(resources, R.drawable.div_line, requireContext().theme)
                    ?: return
            dividerItemDecoration.setDrawable(drawable)
            addItemDecoration(dividerItemDecoration)
        }

    }

    private var canScrollUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            vm.homeList().collect {
                articleListAdapter.submitData(lifecycle, it)
            }
        }
    }
}
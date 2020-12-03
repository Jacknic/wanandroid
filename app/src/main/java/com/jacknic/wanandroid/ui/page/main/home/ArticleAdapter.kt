package com.jacknic.wanandroid.ui.page.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jacknic.wanandroid.data.model.Article
import com.jacknic.wanandroid.databinding.ItemArticleBinding
import com.jacknic.wanandroid.ext.findBinding

/**
 * 文章分页加载适配器
 */
class ArticleAdapter : PagingDataAdapter<Article, ViewHolder>(ITEM_COMPARATOR) {

    var onClickListener: ((position: Int, article: Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(inflater, parent, false)
        return object : ViewHolder(binding.root) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.itemView.apply {
                findBinding<ItemArticleBinding>()?.let { it.article = article }
                setOnClickListener { onClickListener?.invoke(position, article) }
            }
        }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id
        }
    }
}

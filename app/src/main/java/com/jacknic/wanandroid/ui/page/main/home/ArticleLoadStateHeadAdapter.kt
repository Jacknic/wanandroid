package com.jacknic.wanandroid.ui.page.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jacknic.wanandroid.databinding.IncludeReloadViewBinding
import com.orhanobut.logger.Logger

/**
 * @author Jacknic
 */
class ArticleLoadStateHeadAdapter : LoadStateAdapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        Logger.d("当前加载状态：${loadState}")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IncludeReloadViewBinding.inflate(inflater, parent, false)
        val message: String
        when (loadState) {
            is LoadState.Loading -> {
                message = "正在加载"
                Logger.d(message)
            }
            is LoadState.Error -> {
                message = "加载失败"
                Logger.d(message)
            }
            else -> {
                message = "无操作----"
                Logger.d(message)
            }
        }
        binding.tvMessage.text = message
        return object : RecyclerView.ViewHolder(binding.root) {}
    }
}
package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jacknic.android.core.domain.data.WanRepository
import com.jacknic.android.core.model.Article
import com.jacknic.android.core.model.Banner
import com.jacknic.android.wanandroid.BuildConfig
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.TLog
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.util.PagingListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 首页视图数据
 *
 * @author Jacknic
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private var savedStateHandle: SavedStateHandle,
    private var repo: WanRepository
) : ViewModel() {
    private val log = TLog.create("HomeViewModel", BuildConfig.DEBUG)
    private var loadPagingDataJob: Job? = null
    private val _articleListFlow = MutableStateFlow(PagingData.empty<Article>())
    val articleListFlow = _articleListFlow.cachedIn(viewModelScope)
    private val _bannerList = MutableStateFlow<StateResult<List<Banner>>>(StateResult.Loading)
    val bannerList = _bannerList.asStateFlow()

    init {
        getBannerList()
        loadPagingData()
    }

    private fun getBannerList() {
        viewModelScope.launch {
            _bannerList.emit(repo.getHomeBannerList().toStateResult())
        }
    }

    /**
     * 加载分页数据
     */
    fun loadPagingData() {
        log.tag().d("loadPagingData: 加载分页数据")
        loadPagingDataJob = viewModelScope.launch {
            PagingListDataSource.pager(repo::getHomeArticleList)
                .flow
                .collect {
                    _articleListFlow.value = it
                }
        }
    }

    override fun onCleared() {
        log.tag().d("onCleared: HomeViewModel")
    }
}
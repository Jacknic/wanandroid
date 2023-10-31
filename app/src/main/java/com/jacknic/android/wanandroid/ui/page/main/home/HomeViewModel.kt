package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jacknic.android.core.domain.data.WanRepository
import com.jacknic.android.core.model.Article
import com.jacknic.android.wanandroid.BuildConfig
import com.jacknic.android.wanandroid.core.common.TLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val pageSize = 30
    private var loadPagingDataJob: Job? = null
    private val _articleListFlow = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val articleListFlow: StateFlow<PagingData<Article>> = _articleListFlow.asStateFlow()

    init {
        loadPagingData()
    }

    /***
     * 加载分页数据
     */
    fun loadPagingData() {
        log.tag().d("loadPagingData: 加载分页数据")
        loadPagingDataJob = viewModelScope.launch {
            val pagingConfig = PagingConfig(pageSize, initialLoadSize = pageSize)
            Pager(pagingConfig) {
                HomeArticleListDataSource(repo)
            }.flow.cachedIn(viewModelScope).collect {
                _articleListFlow.value = it
            }
        }
    }

    override fun onCleared() {
        log.tag().d("onCleared: HomeViewModel")
    }
}
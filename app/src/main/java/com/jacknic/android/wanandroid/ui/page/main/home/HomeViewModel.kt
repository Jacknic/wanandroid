package com.jacknic.android.wanandroid.ui.page.main.home


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jacknic.android.wanandroid.BuildConfig
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.TLog
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Banner
import com.jacknic.android.wanandroid.core.model.Chapter
import com.jacknic.android.wanandroid.util.PagingListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    private val _bannerList = MutableStateFlow<StateResult<List<Banner>>>(StateResult.Loading)
    val bannerList = _bannerList.asStateFlow()

    private val _categories = MutableStateFlow<StateResult<List<Chapter>>>(StateResult.Loading)
    val categories = _categories.asStateFlow()

    private val _targetCid = MutableStateFlow<Int?>(null)
    val targetCid = _targetCid.asStateFlow()

    /**
     * 导航到指定分类
     */
    fun navigateToCategory(cid: Int) {
        _targetCid.value = cid
    }

    /**
     * 消费目标分类ID
     */
    fun consumeTargetCid() {
        _targetCid.value = null
    }

    private val pagingFlows = mutableMapOf<Int, Flow<PagingData<Article>>>()

    init {
        getBannerList()
        getCategories()
    }

    /**
     * 获取指定分类的文章列表流
     */
    fun getArticleListFlow(cid: Int): Flow<PagingData<Article>> {
        return pagingFlows.getOrPut(cid) {
            log.tag().d("getArticleListFlow: 创建分页流 cid=$cid")
            PagingListDataSource.pager(
                loadAction = { page, pageSize ->
                    if (cid == -1) {
                        repo.getHomeArticleList(page, pageSize, null)
                    } else {
                        repo.getProjectList(page, cid)
                    }
                }
            ).flow.cachedIn(viewModelScope)
        }
    }

    private fun getBannerList() {
        viewModelScope.launch {
            _bannerList.emit(repo.getHomeBannerList().toStateResult())
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            val result = repo.getProjectTree().toStateResult()
            if (result is StateResult.Success) {
                // 添加"全部分类"或者"推荐"
                val allCategory = Chapter(name = "推荐", id = -1)
                _categories.emit(StateResult.Success(listOf(allCategory) + result.data))
            } else {
                _categories.emit(result)
            }
        }
    }

    fun refresh() {
        getBannerList()
        getCategories()
    }

    override fun onCleared() {
        log.tag().d("onCleared: HomeViewModel")
    }
}

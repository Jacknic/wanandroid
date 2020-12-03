package com.jacknic.wanandroid.ui.page.main.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jacknic.wanandroid.data.model.Article
import com.jacknic.wanandroid.data.remote.repository.WanRepository
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * 文章列表
 *
 * @author Jacknic
 */
@ActivityScoped
class ArticleListViewModel @ViewModelInject constructor(
    private val wanRepository: WanRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currPage = 0
    private val articleList = mutableListOf<Article>()
    private val _articleList = MutableLiveData<List<Article>>()
    private var fetchJob: Job? = null

    val articles: LiveData<List<Article>> = _articleList

    fun fetchArticle() {
        if (fetchJob?.isActive == true) {
            return
        }
        fetchJob = viewModelScope.launch {
            wanRepository.fetchHomeList(currPage)
                .doSuccess {
                    articleList.addAll(it.datas)
                    _articleList.postValue(articleList)
                }.doError {
                    System.err.println("获取失败")
                    it.printStackTrace()
                }
        }
    }

    private var currentSearchResult: Flow<PagingData<Article>>? = null

    fun homeList(): Flow<PagingData<Article>> {
        if (currentSearchResult != null) {
            return currentSearchResult as Flow<PagingData<Article>>
        }
        val pageFlow: Flow<PagingData<Article>> = wanRepository.fetchHomeList2()
            .flow.cachedIn(viewModelScope)
        currentSearchResult = pageFlow
        return pageFlow
    }
}
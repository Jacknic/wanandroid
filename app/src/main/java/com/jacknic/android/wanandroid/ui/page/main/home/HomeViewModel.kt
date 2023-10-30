package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.core.domain.data.WanRepository
import com.jacknic.android.core.model.Article
import com.jacknic.android.wanandroid.BuildConfig
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.TLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val _articleList = MutableLiveData<StateResult<List<Article>>>()
    private var getHomeArticleListJob: Job? = null

    val articleList: LiveData<StateResult<List<Article>>> = _articleList

    init {
        getHomeArticleList()
    }

    fun getHomeArticleList() {
        log.tag().d("create: HomeViewModel")
        getHomeArticleListJob?.cancel()
        getHomeArticleListJob = viewModelScope.launch {
            val result = repo.getHomeArticleList(0, 30)
            result.onSuccess {
                _articleList.postValue(StateResult.Success(it.datas))
            }.onFailure {
                _articleList.postValue(StateResult.Error(it))
            }
        }
    }

    override fun onCleared() {
        log.tag().d("onCleared: HomeViewModel")
    }
}
package com.jacknic.android.wanandroid.ui.page.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jacknic.android.wanandroid.BuildConfig
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.TLog
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.data.UserDataRepository
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.HotKeyword
import com.jacknic.android.wanandroid.util.PagingListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SEARCH_KEY = "search_key"

/**
 * 搜索页视图数据
 *
 * @author Jacknic
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repo: WanRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val log = TLog.create("SearchViewModel", BuildConfig.DEBUG)
    private val _searchResultFlow = MutableStateFlow(PagingData.empty<Article>())
    val searchResultFlow = _searchResultFlow.cachedIn(viewModelScope)

    private val _searchKey = MutableStateFlow("")
    val searchKey = _searchKey.asStateFlow()

    private val _hotkeyResult = MutableStateFlow<StateResult<List<HotKeyword>>>(StateResult.Loading)
    val hotkeyResult = _hotkeyResult.asStateFlow()

    val searchHistory = userDataRepository.searchHistoryFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        getHotkey()
        savedStateHandle.get<String>(SEARCH_KEY)?.let {
            if (it.isNotEmpty()) {
                search(it)
            }
        }
    }

    private fun getHotkey() {
        viewModelScope.launch {
            _hotkeyResult.emit(repo.getHotkey().toStateResult())
        }
    }

    /**
     * 设置搜索关键词并执行搜索
     */
    fun setSearchKey(key: String) {
        if (key.isNotEmpty() && key != _searchKey.value) {
            search(key)
        }
    }

    /**
     * 清空搜索
     */
    fun clearSearch() {
        _searchKey.value = ""
        _searchResultFlow.value = PagingData.empty()
    }

    /**
     * 搜索
     */
    fun search(key: String) {
        _searchKey.value = key
        log.tag().d("search: $key")
        viewModelScope.launch {
            userDataRepository.addSearchHistory(key)
        }
        viewModelScope.launch {
            PagingListDataSource.pager(
                loadAction = { page, pageSize ->
                    repo.searchArticles(page, key, pageSize)
                }
            ).flow.collect {
                _searchResultFlow.value = it
            }
        }
    }

    /**
     * 删除单条历史
     */
    fun removeHistory(key: String) {
        viewModelScope.launch {
            userDataRepository.removeSearchHistory(key)
        }
    }

    /**
     * 清空全部历史
     */
    fun clearHistory() {
        viewModelScope.launch {
            userDataRepository.clearSearchHistory()
        }
    }
}

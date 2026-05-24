package com.jacknic.android.wanandroid.ui.page.main.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Tree
import com.jacknic.android.wanandroid.util.PagingListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 分类页视图数据
 */
@HiltViewModel
class CategoryViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle, private val repo: WanRepository) : ViewModel() {

    private val _treeResult = MutableStateFlow<StateResult<List<Tree>>>(StateResult.Loading)
    val treeResult = _treeResult.asStateFlow()

    /** 当前搜索关键字 */
    private val _searchQuery = MutableStateFlow(savedStateHandle[KEY_SEARCH_QUERY] ?: "")
    val searchQuery = _searchQuery.asStateFlow()

    /** 展开的一级分类索引集合 */
    private val _expandedIndices = MutableStateFlow<Set<Int>>(
        savedStateHandle.get<String>(KEY_EXPANDED_INDICES)?.split(",")
            ?.filter { it.isNotEmpty() }?.mapNotNull { it.toIntOrNull() }?.toSet()
            ?: emptySet(),
    )
    val expandedIndices = _expandedIndices.asStateFlow()

    /** 当前选中的二级分类ID */
    private val _selectedChildId = MutableStateFlow<Int?>(savedStateHandle[KEY_SELECTED_CHILD_ID])
    val selectedChildId = _selectedChildId.asStateFlow()

    private val pagingFlows = mutableMapOf<Int, Flow<PagingData<Article>>>()

    init {
        if (_treeResult.value is StateResult.Loading) {
            loadTree()
        }
    }

    private fun loadTree() {
        viewModelScope.launch {
            _treeResult.emit(repo.getTree().toStateResult())
        }
    }

    /**
     * 切换一级分类的展开/折叠状态
     */
    fun toggleExpand(index: Int) {
        _expandedIndices.value = _expandedIndices.value.toMutableSet().apply {
            if (contains(index)) remove(index) else add(index)
        }
        saveExpandedIndices()
    }

    /**
     * 选中二级分类
     */
    fun selectChild(chapterId: Int) {
        _selectedChildId.value = chapterId
        savedStateHandle[KEY_SELECTED_CHILD_ID] = chapterId
    }

    /**
     * 更新搜索关键字
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        savedStateHandle[KEY_SEARCH_QUERY] = query
    }

    /**
     * 根据搜索关键字筛选体系数据
     */
    fun filterTrees(trees: List<Tree>): List<Tree> {
        val query = _searchQuery.value.trim()
        if (query.isEmpty()) return trees
        return trees.mapNotNull { tree ->
            val nameMatch = tree.name.contains(query, ignoreCase = true)
            val matchedChildren = tree.children.filter {
                it.name.contains(query, ignoreCase = true)
            }
            if (nameMatch) {
                tree // 一级分类名匹配，保留全部子分类
            } else if (matchedChildren.isNotEmpty()) {
                tree.copy(children = matchedChildren)
            } else {
                null
            }
        }
    }

    /**
     * 获取指定分类的文章列表流
     */
    fun getArticleListFlow(cid: Int): Flow<PagingData<Article>> = pagingFlows.getOrPut(cid) {
        PagingListDataSource.pager(
            loadAction = { page, pageSize ->
                repo.getHomeArticleList(page, pageSize, cid.toString())
            },
        ).flow.cachedIn(viewModelScope)
    }

    fun refresh() {
        loadTree()
    }

    // ==================== 状态保存与恢复 ====================

    fun saveTreeScrollState(firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) {
        savedStateHandle[KEY_TREE_SCROLL_INDEX] = firstVisibleItemIndex
        savedStateHandle[KEY_TREE_SCROLL_OFFSET] = firstVisibleItemScrollOffset
    }

    fun getTreeScrollState(): Pair<Int, Int> {
        val index = savedStateHandle[KEY_TREE_SCROLL_INDEX] ?: 0
        val offset = savedStateHandle[KEY_TREE_SCROLL_OFFSET] ?: 0
        return index to offset
    }

    fun saveDetailScrollState(cid: Int, firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) {
        savedStateHandle["${KEY_DETAIL_SCROLL_PREFIX}${cid}_index"] = firstVisibleItemIndex
        savedStateHandle["${KEY_DETAIL_SCROLL_PREFIX}${cid}_offset"] = firstVisibleItemScrollOffset
    }

    fun getDetailScrollState(cid: Int): Pair<Int, Int> {
        val index = savedStateHandle["${KEY_DETAIL_SCROLL_PREFIX}${cid}_index"] ?: 0
        val offset = savedStateHandle["${KEY_DETAIL_SCROLL_PREFIX}${cid}_offset"] ?: 0
        return index to offset
    }

    fun saveIsSearching(isSearching: Boolean) {
        savedStateHandle[KEY_IS_SEARCHING] = isSearching
    }

    fun getIsSearching(): Boolean = savedStateHandle[KEY_IS_SEARCHING] ?: false

    private fun saveExpandedIndices() {
        savedStateHandle[KEY_EXPANDED_INDICES] = _expandedIndices.value.joinToString(",")
    }

    companion object {
        private const val KEY_SEARCH_QUERY = "category_search_query"
        private const val KEY_EXPANDED_INDICES = "category_expanded_indices"
        private const val KEY_SELECTED_CHILD_ID = "category_selected_child_id"
        private const val KEY_IS_SEARCHING = "category_is_searching"
        private const val KEY_TREE_SCROLL_INDEX = "category_tree_scroll_index"
        private const val KEY_TREE_SCROLL_OFFSET = "category_tree_scroll_offset"
        private const val KEY_DETAIL_SCROLL_PREFIX = "category_detail_scroll_"
    }
}

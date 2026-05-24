package com.jacknic.android.wanandroid.ui.page.readinghistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.ui.component.ReadingHistoryManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ReadingHistoryViewModel @Inject constructor(private val readingHistoryManager: ReadingHistoryManager) : ViewModel() {

    val readingHistoryList = readingHistoryManager.readingHistoryFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun removeReadingHistory(articleId: Int) {
        viewModelScope.launch {
            readingHistoryManager.removeReadingHistory(articleId)
        }
    }

    fun clearReadingHistory() {
        viewModelScope.launch {
            readingHistoryManager.clearReadingHistory()
        }
    }
}

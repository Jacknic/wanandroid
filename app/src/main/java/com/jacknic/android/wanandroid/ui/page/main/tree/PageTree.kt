@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.jacknic.android.wanandroid.ui.page.main.tree

import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentItem(val text: String) : Parcelable

@Composable
fun PageTree(
    scaffoldNavigator: ThreePaneScaffoldNavigator<ContentItem> = rememberListDetailPaneScaffoldNavigator<ContentItem>(),
    state: LazyListState = rememberLazyListState()
) {
    val scope = rememberCoroutineScope()
    ListDetailPaneScaffold(
        directive = scaffoldNavigator.scaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        listPane = {
            LazyColumn(
                state = state,
                contentPadding = WindowInsets.statusBars.asPaddingValues()
            ) {
                items(100) {
                    val text = "List item $it"
                    ListItem(
                        headlineContent = { Text(text) },
                        modifier = Modifier.clickable {
                            scope.launch {
                                scaffoldNavigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    ContentItem(text)
                                )
                            }
                        })
                }
            }
        },
        detailPane = {
            val content = scaffoldNavigator.currentDestination?.contentKey
            if (content != null) {
                Surface(
                    modifier = Modifier.systemBarsPadding(),
                    onClick = {
                        scope.launch {
                            scaffoldNavigator.navigateBack()
                        }
                    }
                ) {
                    Text("Details => ${content.text}")
                }
            }
        }
    )
}
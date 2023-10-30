package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.KeyboardArrowRight
import androidx.compose.material.icons.twotone.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PageHome(
    pageScrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
) {
    val nav = LocalNavCtrl.current
    val stackEntry = nav.currentBackStackEntry!!
    val vm: HomeViewModel = hiltViewModel(stackEntry)
    val dataList = vm.articleList.observeAsState(StateResult.Loading)
    LazyColumn(
        Modifier.nestedScroll(pageScrollBehavior.nestedScrollConnection),
        state = listState
    ) {
        item {
            Text("当前页面：")
        }
        item {
            HorizontalPager(
                pageCount = 5, modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.inversePrimary, CircleShape)
            ) {
                Image(Icons.TwoTone.ThumbUp, "", modifier = Modifier.fillMaxSize())
            }
        }
        when (val items = dataList.value) {
            is StateResult.Error -> {
                item {
                    Button(onClick = { vm.getHomeArticleList() }) {
                        Text("加载失败")
                    }
                }
            }

            StateResult.Loading -> {
                item {
                    Text("加载中")
                }
            }

            is StateResult.Success -> {
                val articles = items.data
                items(articles.size) {
                    val article = articles[it]

                    ListItem(
                        leadingContent = { Text(article.title.firstOrNull().toString()) },
                        headlineText = {
                            Text(
                                article.title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        trailingContent = {
                            Icon(Icons.TwoTone.KeyboardArrowRight, "")
                        }
                    )
                }
            }
        }
    }
}
package com.jacknic.android.wanandroid.ui.page.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSearch() {
    Scaffold(topBar = {
        TopAppBar(title = { Text("搜索") })
    }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                Text("搜索页")
            }
        }
    }
}
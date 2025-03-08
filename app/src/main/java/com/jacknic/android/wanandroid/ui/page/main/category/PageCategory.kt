package com.jacknic.android.wanandroid.ui.page.main.category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jacknic.android.wanandroid.ui.component.PreviewUserLevelCardPager

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageCategory() {
    Scaffold {
        Column(Modifier.padding(it)) {
            PreviewUserLevelCardPager()
        }
    }
}
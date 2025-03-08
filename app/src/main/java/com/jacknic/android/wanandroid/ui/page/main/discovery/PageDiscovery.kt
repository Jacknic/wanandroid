package com.jacknic.android.wanandroid.ui.page.main.discovery

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jacknic.android.wanandroid.ui.component.PreviewUserLevelCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageDiscovery() {
    Scaffold {
        Scaffold {
            Column(Modifier.padding(it)) {
                PreviewUserLevelCard()
            }
        }
    }
}
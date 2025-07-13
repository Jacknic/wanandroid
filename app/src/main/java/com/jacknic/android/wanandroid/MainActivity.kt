package com.jacknic.android.wanandroid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jacknic.android.wanandroid.core.common.TLog
import com.jacknic.android.wanandroid.ui.page.AppNavHost
import com.jacknic.android.wanandroid.ui.theme.WanandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val log = TLog.create("MainActivity", BuildConfig.DEBUG)

    override fun onCreate(savedInstanceState: Bundle?) {
        log.tag().d("onCreate: MainActivity")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WanandroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Unspecified
                ) {
                    AppNavHost()
                }
            }
        }
    }
}
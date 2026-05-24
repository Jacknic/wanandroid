package com.jacknic.android.wanandroid

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jacknic.android.wanandroid.core.common.TLog
import com.jacknic.android.wanandroid.ui.component.CollectStateManager
import com.jacknic.android.wanandroid.ui.component.ReadingHistoryManager
import com.jacknic.android.wanandroid.ui.page.AppNavHost
import com.jacknic.android.wanandroid.ui.theme.WanandroidTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val log = TLog.create("MainActivity", BuildConfig.DEBUG)

    @Inject
    lateinit var collectStateManager: CollectStateManager

    @Inject
    lateinit var readingHistoryManager: ReadingHistoryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        log.tag().d("onCreate: MainActivity")
        setTheme(R.style.Theme_Wanandroid)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            WanandroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Unspecified,
                ) {
                    AppNavHost(
                        collectStateManager = collectStateManager,
                        readingHistoryManager = readingHistoryManager,
                    )
                }
            }
        }
    }
}

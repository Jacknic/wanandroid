package com.jacknic.android.wanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.jacknic.android.wanandroid.core.common.TLog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val log = TLog.create("MainActivity", BuildConfig.DEBUG)

    override fun onCreate(savedInstanceState: Bundle?) {
        log.tag().d("onCreate: MainActivity")
        super.onCreate(savedInstanceState)
    }
}
package com.jacknic.wanandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkInfo
import com.jacknic.wanandroid.R
import com.jacknic.wanandroid.workers.UpdateCheckWorker
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val topIds = setOf(R.id.mainPage)
    private val appBarConfiguration = AppBarConfiguration.Builder(topIds).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeDay)
        setTheme(R.style.AppThemeOverlay_Theme)
//        setTheme(R.style.AppThemeOverlay_Toolbar_Primary)
        setTheme(R.style.AppThemeOverlay_PrimaryPalette_Purple)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        listenCheckUpdateState()
    }

    private fun listenCheckUpdateState() {
        UpdateCheckWorker.start(this)
        UpdateCheckWorker.getWorkInfo(this).observe(this, Observer { workInfoList ->
            workInfoList.forEach {
                when (it.state) {
                    WorkInfo.State.RUNNING -> {
                        Logger.d("正在检测更新")
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Logger.d("检测成功")
                    }
                    WorkInfo.State.FAILED -> {
                        Logger.w("检测失败")
                    }
                    else -> {

                    }
                }
            }

        })

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        val navCtrl = findNavController(R.id.pager)
        toolbar.setupWithNavController(navCtrl, appBarConfiguration)
    }
}

package com.jacknic.android.wanandroid

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.jacknic.android.wanandroid.core.common.TLog
import com.jacknic.android.wanandroid.core.data.UserDataRepository
import com.jacknic.android.wanandroid.ui.theme.initThemeSettings
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

/**
 * 应用入口
 *
 * @author Jacknic
 */
@HiltAndroidApp
class App : Application(), ImageLoaderFactory {
    private val log = TLog.create("App", BuildConfig.DEBUG)
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    @Inject
    lateinit var userDataRepository: UserDataRepository

    override fun onCreate() {
        super.onCreate()
        log.tag().d("onCreate: App created")
        appScope.launch { initThemeSettings(userDataRepository) }
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}

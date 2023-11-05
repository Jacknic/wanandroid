package com.jacknic.android.wanandroid

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.jacknic.android.wanandroid.core.common.TLog
import dagger.hilt.android.HiltAndroidApp
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

    @Inject
    lateinit var imageLoader: Provider<ImageLoader>
    override fun onCreate() {
        super.onCreate()
        log.tag().d("onCreate: App created")
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
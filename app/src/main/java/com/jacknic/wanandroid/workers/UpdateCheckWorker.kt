package com.jacknic.wanandroid.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.*
import androidx.work.ExistingPeriodicWorkPolicy.KEEP
import androidx.work.ExistingPeriodicWorkPolicy.REPLACE
import com.jacknic.wanandroid.data.remote.repository.AppUpdateRepository
import com.jacknic.wanandroid.data.util.SafeResult
import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

/**
 * 升级更新检测
 *
 * @author Jacknic
 */
class UpdateCheckWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val updateRepo: AppUpdateRepository
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Logger.d("检测应用更新")
        return runBlocking {
            val result = updateRepo.check()
            if (result is SafeResult.Success) {
                val data = Data.Builder()
                    .putBoolean("isNew", true)
                    .build()
                return@runBlocking Result.success(data)
            } else {
                return@runBlocking Result.failure()
            }
        }
    }

    companion object {

        private const val NAME_UPDATE_CHECK_WORKER = "update_check_worker"

        /**
         * 启动检测更新任务
         */
        fun start(context: Context, replace: Boolean = false) {
            val workManager = WorkManager.getInstance(context)
            val constraints = Constraints.Builder()
                .build()
            val checkRequest =
                PeriodicWorkRequestBuilder<UpdateCheckWorker>(15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build()
            val existingPeriodicWorkPolicy = if (replace) REPLACE else KEEP
            workManager.enqueueUniquePeriodicWork(
                NAME_UPDATE_CHECK_WORKER,
                existingPeriodicWorkPolicy,
                checkRequest
            )
        }

        /**
         * 获取当前任务信息
         */
        fun getWorkInfo(context: Context) = run {
            val workManager = WorkManager.getInstance(context)
            workManager.getWorkInfosForUniqueWorkLiveData(NAME_UPDATE_CHECK_WORKER)
        }
    }
}
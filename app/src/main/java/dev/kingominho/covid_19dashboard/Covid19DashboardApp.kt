package dev.kingominho.covid_19dashboard

import android.app.Application
import android.util.Log
import androidx.work.*
import dev.kingominho.covid_19dashboard.worker.NotifyUserWorker
import dev.kingominho.covid_19dashboard.worker.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class Covid19DashboardApp : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        //.setRequiresCharging(true)
//        .apply {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                setRequiresDeviceIdle(true)
//            }
//        }
        .build()

    private val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
        1,
        TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    private val repeatingNotificationRequest = PeriodicWorkRequestBuilder<NotifyUserWorker>(
        1,
        TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    override fun onCreate() {
        super.onCreate()
        Log.d("Tag", "MainActivity: Inside onCreate()")
        delayedInit()
    }

    private fun delayedInit() {
        Log.d("Tag" ,"Covid19DashboardApp: inside delayedInit()")
        applicationScope.launch {
            Log.d("Tag" ,"Covid19DashboardApp: inside applicationScope.launch{ ... }")
            setupRefreshWork()
            setupNotifyWork()
        }
    }

    private fun setupNotifyWork() {
        Log.d("Covid19App:","Notify: Constraints built. ${constraints.toString()}")
        Log.d("Covid19App:","Notify: Repeating request built. Id: ${repeatingNotificationRequest.id}")

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            NotifyUserWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingNotificationRequest
        )
        Log.d("Covid19App:","Notify: WorkManager enqueued repeatingNotificationRequest")
    }

    private fun setupRefreshWork() {
        Log.d("Covid19App:","Network: Constraints built. ${constraints.toString()}")
        Log.d("Covid19App:","Network: Repeating request built. Id: ${repeatingRequest.id}")

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
        Log.d("Covid19App:","Network: WorkManager enqueued repeatingRequest")
    }
}
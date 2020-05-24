package dev.kingominho.covid_19dashboard.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.kingominho.covid_19dashboard.R
import dev.kingominho.covid_19dashboard.database.getDatabase
import dev.kingominho.covid_19dashboard.repository.Covid19DataRepository
import dev.kingominho.covid_19dashboard.ui.MainActivity
import dev.kingominho.covid_19dashboard.util.formatNumber
import dev.kingominho.covid_19dashboard.util.getPeriod
import dev.kingominho.covid_19dashboard.util.toDateFormat
import retrofit2.HttpException
import java.text.SimpleDateFormat

class NotifyUserWorker (
    private val appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {


    companion object {
        const val WORK_NAME = "Covid19NotifyUserWorker"
    }

    private fun showNotification(totalCount: Int, time: String) {
        val contentTitle: String = if (totalCount == -1) {
            appContext.getString(R.string.notification_content_title_on_data_fetch_failure)
        } else {
            appContext.getString(R.string.notification_title_format, totalCount.formatNumber())
        }
        val intent = Intent(appContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            appContext, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = appContext.getString(R.string.default_notification_channel_id)
        val channelName = appContext.getString(R.string.default_notification_channel_name)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(appContext, channelId)
            .setColor(ContextCompat.getColor(appContext, R.color.primaryColor))
            .setSmallIcon(R.drawable.ic_alert)
            .setContentTitle(contentTitle)
            .setContentText(appContext.getString(R.string.last_updated_format, time))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }


    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = Covid19DataRepository(database)

        return try {
            repository.refreshSummaries()
            try {
                val global = repository.getSummary("global")
                val totalCount = global.value?.totalConfirmed ?: -1
                val time = global.value?.date?.toDateFormat()?.let {
                    getPeriod(
                        it
                    )
                } ?: SimpleDateFormat.getDateTimeInstance().format(System.currentTimeMillis())
                showNotification(totalCount, time)
                Result.success()
            }
            catch (exception: Exception) {
                Log.d("Tag", "Failed to notify user. Error: ", exception)
                Result.retry()
            }
        } catch (exception: HttpException) {
            val errorMessage = "Http Error: " +
                    "code: ${exception.code()}" +
                    "response: ${exception.response()}" +
                    "message: ${exception.message()}"
            Log.e("Tag", "Error: $errorMessage", exception)
            Result.retry()
        }
    }
}
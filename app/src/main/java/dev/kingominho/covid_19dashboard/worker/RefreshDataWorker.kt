package dev.kingominho.covid_19dashboard.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.kingominho.covid_19dashboard.database.getDatabase
import dev.kingominho.covid_19dashboard.repository.Covid19DataRepository
import retrofit2.HttpException
import java.lang.Exception

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "Covid19RefreshDataWorker"
    }


    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = Covid19DataRepository(database)

        return try {
            repository.refreshSummaries()
            Result.success()
        } catch (exception: HttpException) {
            val errorMessage = "Http Error: " +
                    "code: ${exception.code()}" +
                    "response: ${exception.response()}" +
                    "message: ${exception.message()}"
            Log.e("Tag", "RefreshDataWork: HttpException: $errorMessage :: ", exception)
            Result.retry()
        } catch (exception: Exception) {
            Result.failure()
        }
    }
}
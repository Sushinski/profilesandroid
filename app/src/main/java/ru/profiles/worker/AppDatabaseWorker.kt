package ru.profiles.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.profiles.data.AppDatabase

class AppDatabaseWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            val database = AppDatabase.getInstance(applicationContext)
            // todo insert preconfigured values
            Result.SUCCESS
        } catch (ex: Exception) {
            Log.e("ProfilesError", "Error seeding database", ex)
            Result.FAILURE
        }
    }
}
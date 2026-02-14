package hr.algebra.f1_app.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class F1Worker(private val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {
    override fun doWork(): Result {
        F1Fetcher(context).fetchDrivers()
        return Result.success()
    }
}
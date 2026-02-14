package hr.algebra.f1_app.api


import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class F1Worker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.d("F1Worker", "🚀 F1Worker STARTED - pozivam F1Fetcher!")

        try {
            F1Fetcher(context).fetchDrivers()
            Log.d("F1Worker", "✅ F1Worker SUCCESS - F1Fetcher završen!")
            return Result.success()
        } catch (e: Exception) {
            Log.e("F1Worker", "❌ F1Worker FAILED: ${e.message}", e)
            return Result.retry()
        }
    }
}


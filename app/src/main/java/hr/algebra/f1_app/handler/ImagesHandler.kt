package hr.algebra.f1_app.handler

import android.content.Context
import android.util.Log
import hr.algebra.f1_app.factory.createHttpUrlCon
import java.io.File
import java.net.HttpURLConnection
import java.nio.file.Files

fun download(context: Context, url: String, driverCode: String): String? {
    val filename = "${driverCode}_${System.currentTimeMillis()}.png"  // ALB_1645123456.png
    val file = createFile(context, filename)

    try {
        val con = createHttpUrlCon(url)
        Files.copy(con.inputStream, file.toPath())
        Log.d("Download", "✅ $driverCode -> $filename")
        return file.absolutePath
    } catch (e: Exception) {
        Log.e("Download", "❌ $driverCode fail: ${e.message}")
        null
    }
    return null
}

fun createFile(context: Context, filename: String): File {
    val dir = File(context.getExternalFilesDir(null), "drivers")
    dir.mkdirs()
    return File(dir, filename)  // ← BEZ delete()!
}

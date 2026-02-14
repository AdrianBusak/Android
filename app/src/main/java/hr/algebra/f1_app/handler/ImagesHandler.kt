package hr.algebra.f1_app.handler

import android.content.Context
import android.util.Log
import hr.algebra.f1_app.factory.createHttpUrlCon
import java.io.File
import java.net.HttpURLConnection
import java.nio.file.Files

fun download(context: Context, url: String?, driverCode: String) : String?{
    if(url == null) return null
    val filename = "$driverCode.png"
    val file: File = createFile(context, filename)
    try {
        val con: HttpURLConnection = createHttpUrlCon(url)
        Files.copy(con.getInputStream(), file.toPath())
        return file.absolutePath
    } catch (e: Exception) {
        Log.e("ERROR", e.toString(), e)
    }
    return null
}

fun createFile(context: Context, filename: String): File {
    val dir = context.getExternalFilesDir(null)
    val file = File(dir, filename)
    if(file.exists()) file.delete()
    return file
}
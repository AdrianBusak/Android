package hr.algebra.f1_app.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.f1_app.provider.F1_PROVIDER_CONTENT_URI
import hr.algebra.f1_app.F1Receiver
import hr.algebra.f1_app.framework.sendBroadcast
import hr.algebra.f1_app.handler.download
import hr.algebra.f1_app.model.F1Driver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File

class F1Fetcher(private val context: Context) {

    private val f1Api: F1Api

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        f1Api = retrofit.create<F1Api>()
    }

    fun fetchDrivers() {
        val request = f1Api.fetchDrivers()

        request.enqueue(object : Callback<List<F1DriverDto>> {
            override fun onResponse(
                call: Call<List<F1DriverDto>>,
                response: Response<List<F1DriverDto>>
            ) {
                populateDrivers(response.body())
            }

            override fun onFailure(call: Call<List<F1DriverDto>>, t: Throwable) {
                Log.e("ERROR", t.toString(), t)
            }
        })
    }

    private fun populateDrivers(dtoList: List<F1DriverDto>?) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            dtoList?.forEach { dto ->
                val headshotPath = download(context, dto.headshotUrl, dto.driverNumber.toString()) ?: ""

                Log.d("F1Fetcher", "Driver: ${dto.fullName}, headshotPath: '$headshotPath'") // DEBUG

                val values = ContentValues().apply {  // ← RENAME u values
                    put(F1Driver::fullName.name, dto.fullName)
                    put(F1Driver::firstName.name, dto.firstName)
                    put(F1Driver::lastName.name, dto.lastName)
                    put(F1Driver::code.name, dto.nameAcronym)
                    put(F1Driver::teamName.name, dto.teamName)
                    put(F1Driver::teamColour.name, dto.teamColour)
                    put(F1Driver::driverNumber.name, dto.driverNumber)
                    put(F1Driver::headshotPath.name, headshotPath)  // ← SPAŠAVA SLLIKE!
                    put(F1Driver::meetingKey.name, dto.meetingKey)
                    put(F1Driver::sessionKey.name, dto.sessionKey)
                    put(F1Driver::countryCode.name, dto.countryCode)
                    put(F1Driver::favorite.name, 0)
                }

                try {
                    val uri = context.contentResolver.insert(F1_PROVIDER_CONTENT_URI, values)  // ✅ FIX: values umjesto driverContentValues + context.contentResolver
                    Log.d("F1Fetcher", "✅ Inserted ${dto.fullName} at URI: $uri, photo: $headshotPath")

                    // Provjeri da li datoteka postoji
                    val photoFile = File(headshotPath)
                    Log.d("F1Fetcher", "Photo exists: ${photoFile.exists()}, path: ${photoFile.absolutePath}")

                } catch (e: Exception) {
                    Log.e("F1Fetcher", "❌ INSERT FAILED for ${dto.fullName}: ${e.message}", e)
                }
            }

            // Broadcast NA KRAJU svih insert-a
            context.sendBroadcast<F1Receiver>()
        }
    }

}

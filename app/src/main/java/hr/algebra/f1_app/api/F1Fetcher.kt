package hr.algebra.f1_app.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.f1_app.F1Receiver
import hr.algebra.f1_app.provider.F1_PROVIDER_CONTENT_URI
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

        request.enqueue(object: Callback<List<F1DriverDto>> {
            override fun onResponse(
                call: Call<List<F1DriverDto>?>,
                response: Response<List<F1DriverDto>?>
            ) {
                response.body()?.let { populateDrivers(it) }
            }

            override fun onFailure(
                call: Call<List<F1DriverDto>?>,
                t: Throwable
            ) {
                Log.e("ERROR", t.toString(), t)
            }
        })

    }

    private fun populateDrivers(dtoList: List<F1DriverDto>?) {

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            dtoList?.forEach {
                val headshotPath = download(context, it.headshot_url, it.driver_number.toString())

                val values = ContentValues().apply {
                    put(F1Driver::fullName.name, it.full_name)
                    put(F1Driver::firstName.name, it.first_name)
                    put(F1Driver::lastName.name, it.last_name)
                    put(F1Driver::code.name, it.name_acronym)
                    put(F1Driver::teamName.name, it.team_name)
                    put(F1Driver::teamColour.name, it.team_colour)
                    put(F1Driver::driverNumber.name, it.driver_number)
                    put(F1Driver::headshotPath.name, headshotPath ?: "")
                    put(F1Driver::meetingKey.name, it.meeting_key)
                    put(F1Driver::sessionKey.name, it.session_key)
                    put(F1Driver::countryCode.name, it.country_code)
                    put(F1Driver::favorite.name, false)
                }



                context.contentResolver.insert(
                    F1_PROVIDER_CONTENT_URI,
                    values
                )

            }
            // back to the FG
            context.sendBroadcast<F1Receiver>()
        }
    }

}
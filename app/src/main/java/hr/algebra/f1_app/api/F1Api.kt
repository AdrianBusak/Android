package hr.algebra.f1_app.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://api.openf1.org/v1/"

interface F1Api {

    // /drivers?session_key=latest
    @GET("drivers")
    fun fetchDrivers(
        @Query("session_key") sessionKey: String = "latest"
    ): Call<List<F1DriverDto>>
}
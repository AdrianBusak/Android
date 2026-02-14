package hr.algebra.f1_app.api

import retrofit2.Call
import retrofit2.http.GET


const val API_URL = "https://api.openf1.org/v1/"

interface F1Api {
    @GET("drivers?session_key=9158")
    fun fetchDrivers() : Call<List<F1DriverDto>>
}
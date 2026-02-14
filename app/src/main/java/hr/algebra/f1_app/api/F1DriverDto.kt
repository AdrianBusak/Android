package hr.algebra.f1_app.api

import com.google.gson.annotations.SerializedName

data class F1DriverDto(
    @SerializedName("meeting_key") val meeting_key : Int,
    @SerializedName("session_key") val session_key : Int,
    @SerializedName("driver_number") val driver_number : Int,
    @SerializedName("broadcast_name") val broadcast_name : String,
    @SerializedName("full_name") val full_name : String,
    @SerializedName("name_acronym") val name_acronym : String,
    @SerializedName("team_name") val team_name : String,
    @SerializedName("team_colour") val team_colour : String,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("headshot_url") val headshot_url : String?,
    @SerializedName("country_code") val country_code : String?
)
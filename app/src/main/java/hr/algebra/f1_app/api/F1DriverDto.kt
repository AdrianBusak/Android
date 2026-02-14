package hr.algebra.f1_app.api

import com.google.gson.annotations.SerializedName

data class F1DriverDto(
    @SerializedName("meeting_key") val meetingKey: Int,
    @SerializedName("session_key") val sessionKey: Int,
    @SerializedName("driver_number") val driverNumber: Int,
    @SerializedName("broadcast_name") val broadcastName: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("name_acronym") val nameAcronym: String,
    @SerializedName("team_name") val teamName: String,
    @SerializedName("team_colour") val teamColour: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("headshot_url") val headshotUrl: String,
    @SerializedName("country_code") val countryCode: String?
)

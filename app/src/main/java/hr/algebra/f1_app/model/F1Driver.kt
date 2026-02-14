package hr.algebra.f1_app.model

data class F1Driver(
    var _id: Long?,
    val fullName: String,
    val firstName: String,
    val lastName: String,
    val code: String,
    val teamName: String,
    val teamColour: String,
    val driverNumber: Int,
    val headshotPath: String,
    val meetingKey: Int,
    val sessionKey: Int,
    val countryCode: String?,
    var favorite: Boolean
)
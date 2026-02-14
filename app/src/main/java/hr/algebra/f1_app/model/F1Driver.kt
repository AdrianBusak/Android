package hr.algebra.f1_app.model

data class F1Driver(
    var _id: Long?,          // PK iz baze
    val fullName: String,    // "Lando NORRIS"
    val firstName: String,   // "Lando"
    val lastName: String,    // "Norris"
    val code: String,        // "NOR"
    val teamName: String,    // "McLaren"
    val teamColour: String,  // "F47600"
    val driverNumber: Int,   // 1
    val headshotPath: String,// lokalna slika (filepath)
    val meetingKey: Int,     // 1304
    val sessionKey: Int,     // 11466
    val countryCode: String?,// moze biti null
    var favorite: Boolean    // analogija read
)
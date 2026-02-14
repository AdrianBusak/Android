package hr.algebra.f1_app.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.edit
import androidx.core.content.getSystemService
import hr.algebra.f1_app.provider.F1_PROVIDER_CONTENT_URI
import hr.algebra.f1_app.model.F1Driver

fun View.applyAnimation(id: Int) =
    startAnimation(AnimationUtils.loadAnimation(context, id))

fun Context.setBooleanPreference(key: String, value: Boolean = true) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit {
            putBoolean(key, value)
        }
}

fun Context.getBooleanPreference(key: String): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)
}

fun Context.isOnline(): Boolean {
    val connectivityManager =
        getSystemService<ConnectivityManager>() // compare with ours -> reified T: Any - returns null!
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

@SuppressLint("Range")
fun Context.fetchDrivers(): MutableList<F1Driver> {
    val drivers = mutableListOf<F1Driver>()

    contentResolver.query(
        F1_PROVIDER_CONTENT_URI,
        null,
        null,
        null,
        null
    ).use { rs ->
        while (rs?.moveToNext() == true) {
            drivers.add(
                F1Driver(
                    rs.getLong(rs.getColumnIndex(F1Driver::_id.name)),
                    rs.getString(rs.getColumnIndex(F1Driver::fullName.name)),
                    rs.getString(rs.getColumnIndex(F1Driver::firstName.name)),
                    rs.getString(rs.getColumnIndex(F1Driver::lastName.name)),
                    rs.getString(rs.getColumnIndex(F1Driver::code.name)),
                    rs.getString(rs.getColumnIndex(F1Driver::teamName.name)),
                    rs.getString(rs.getColumnIndex(F1Driver::teamColour.name)),
                    rs.getInt(rs.getColumnIndex(F1Driver::driverNumber.name)),
                    rs.getString(rs.getColumnIndex(F1Driver::headshotPath.name)),
                    rs.getInt(rs.getColumnIndex(F1Driver::meetingKey.name)),
                    rs.getInt(rs.getColumnIndex(F1Driver::sessionKey.name)),
                    rs.getString(rs.getColumnIndex(F1Driver::countryCode.name)),
                    rs.getInt(rs.getColumnIndex(F1Driver::favorite.name)) != 0
                )
            )
        }

    }


    return drivers
}

fun callDelayed(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

inline fun <reified T : Activity> Context.startActivity() = startActivity(
    Intent(
        this,
        T::class.java
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })

inline fun <reified T : Activity> Context.startActivity(
    key: String,
    value: Int

) = startActivity(
    Intent(
        this,
        T::class.java
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(key, value)
    })

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() =
    sendBroadcast(Intent(this, T::class.java))
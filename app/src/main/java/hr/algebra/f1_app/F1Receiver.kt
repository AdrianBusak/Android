package hr.algebra.f1_app


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.f1_app.framework.setBooleanPreference
import hr.algebra.f1_app.framework.startActivity

const val DATA_IMPORTED = "hr.algebra.f1.data_imported"

class F1Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.setBooleanPreference(DATA_IMPORTED)
        context.startActivity<HostActivity>()
    }
}

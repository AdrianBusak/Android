package hr.algebra.f1_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import hr.algebra.f1_app.api.F1Worker
import hr.algebra.f1_app.databinding.ActivitySplashScreenBinding
import hr.algebra.f1_app.framework.applyAnimation
import hr.algebra.f1_app.framework.callDelayed
import hr.algebra.f1_app.framework.getBooleanPreference
import hr.algebra.f1_app.framework.isOnline
import hr.algebra.f1_app.framework.startActivity

private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.f1_app.data_imported"
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()

    }

    private fun startAnimations() {

        binding.tvSplash.applyAnimation(R.anim.blink)
        binding.ivSplash.applyAnimation(R.anim.rotate)

    }

    private fun redirect() {

        if(getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) { startActivity<HostActivity>() }
        } else {
            if(isOnline()) {
                WorkManager.getInstance(this).apply {
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.from(F1Worker::class.java)
                    )
                }
            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(DELAY) { finish() }
            }
        }
    }
}
package hr.algebra.f1_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.f1_app.adapter.DriverPagerAdapter
import hr.algebra.f1_app.databinding.ActivityDriverPagerBinding
import hr.algebra.f1_app.framework.fetchDrivers
import hr.algebra.f1_app.model.F1Driver

const val DRIVER_POS = "hr.algebra.f1.driver_position"

class DriverPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverPagerBinding
    private lateinit var drivers: MutableList<F1Driver>
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initPager()
    }

    private fun init() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        position = intent.getIntExtra(DRIVER_POS, position)
        drivers = fetchDrivers()
    }

    private fun initPager() {
        binding.viewPager2.adapter = DriverPagerAdapter(this, drivers)
        binding.viewPager2.currentItem = position
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}

package hr.algebra.f1_app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import hr.algebra.f1_app.R
import hr.algebra.f1_app.databinding.FragmentRandomDriverBinding
import hr.algebra.f1_app.framework.fetchDrivers
import hr.algebra.f1_app.model.F1Driver
import java.io.File

class RandomDriverFragment : Fragment() {

    private lateinit var binding: FragmentRandomDriverBinding
    private lateinit var allDrivers: List<F1Driver>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomDriverBinding.inflate(inflater, container, false)
        allDrivers = requireContext().fetchDrivers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRandomDriver()

        binding.btnShuffle.setOnClickListener {
            showRandomDriver()
        }
    }

    private fun showRandomDriver() {
        if (allDrivers.isNotEmpty()) {
            val randomDriver = allDrivers.random()

            binding.tvRandomName.text = "${randomDriver.firstName} ${randomDriver.lastName}"
            binding.tvRandomTeam.text = randomDriver.teamName

            if (randomDriver.headshotPath.isNotEmpty()) {
                Picasso.get()
                    .load(File(randomDriver.headshotPath))
                    .placeholder(R.drawable.f1_default_driver)
                    .error(R.drawable.f1_default_driver)
                    .into(binding.ivRandomDriver)
            } else {
                binding.ivRandomDriver.setImageResource(R.drawable.f1_default_driver)
            }
        }
    }
}
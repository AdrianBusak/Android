package hr.algebra.f1_app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.f1_app.adapter.DriverAdapter
import hr.algebra.f1_app.databinding.FragmentFavoriteBinding
import hr.algebra.f1_app.framework.fetchDrivers
import hr.algebra.f1_app.model.F1Driver

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteDrivers: MutableList<F1Driver>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayFavorites()
    }

    override fun onResume() {
        super.onResume()
        displayFavorites()
    }

    private fun displayFavorites() {
        favoriteDrivers = requireContext().fetchDrivers().filter { it.favorite }.toMutableList()

        binding.rvFavoriteDrivers.apply {
            adapter = DriverAdapter(requireContext(), favoriteDrivers)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
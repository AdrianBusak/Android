package hr.algebra.f1_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.f1_app.R
import hr.algebra.f1_app.adapter.DriverAdapter
import hr.algebra.f1_app.databinding.FragmentDriversBinding
import hr.algebra.f1_app.framework.fetchDrivers
import hr.algebra.f1_app.model.F1Driver

class DriversFragment : Fragment() {

    private lateinit var binding: FragmentDriversBinding
    private lateinit var drivers: MutableList<F1Driver>

    // inicijalizACIJSKA
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriversBinding.inflate(inflater, container, false)
        drivers = requireContext().fetchDrivers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvDrivers.apply {
            adapter = DriverAdapter(requireContext(), drivers)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}
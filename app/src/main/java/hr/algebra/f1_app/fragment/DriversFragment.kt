package hr.algebra.f1_app.fragment

import android.content.ContentUris
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.f1_app.adapter.DriverAdapter
import hr.algebra.f1_app.databinding.FragmentDriversBinding
import hr.algebra.f1_app.framework.fetchDrivers
import hr.algebra.f1_app.model.F1Driver
import hr.algebra.f1_app.provider.F1_PROVIDER_CONTENT_URI

class DriversFragment : Fragment() {

    private lateinit var binding: FragmentDriversBinding
    private lateinit var drivers: MutableList<F1Driver>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriversBinding.inflate(inflater, container, false)
        // Inicijalizacija liste podataka
        drivers = requireContext().fetchDrivers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDrivers.apply {
            adapter = DriverAdapter(requireContext(), drivers)
            layoutManager = LinearLayoutManager(requireContext())
        }

        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val driverToDelete = drivers[position]

                requireContext().contentResolver.delete(
                    ContentUris.withAppendedId(F1_PROVIDER_CONTENT_URI, driverToDelete._id!!),
                    null,
                    null
                )

                drivers.removeAt(position)
                binding.rvDrivers.adapter?.notifyItemRemoved(position)

                Toast.makeText(
                    requireContext(),
                    "Obrisan ${driverToDelete.lastName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }).attachToRecyclerView(binding.rvDrivers)
    }
}
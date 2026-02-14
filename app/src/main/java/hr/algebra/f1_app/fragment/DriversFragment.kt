package hr.algebra.f1_app.fragment

import android.app.AlertDialog
import android.content.ContentUris
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.f1_app.R
import hr.algebra.f1_app.adapter.DriverAdapter
import hr.algebra.f1_app.databinding.FragmentDriversBinding
import hr.algebra.f1_app.framework.fetchDrivers
import hr.algebra.f1_app.model.F1Driver
import hr.algebra.f1_app.provider.F1_PROVIDER_CONTENT_URI

class DriversFragment : Fragment() {

    private lateinit var binding: FragmentDriversBinding
    private lateinit var drivers: MutableList<F1Driver>
    private var allDrivers = mutableListOf<F1Driver>() // Backup lista za pretragu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true) // Omogućuje menu u fragmentu
        binding = FragmentDriversBinding.inflate(inflater, container, false)

        // Inicijalizacija podataka
        allDrivers = requireContext().fetchDrivers()
        drivers = allDrivers.toMutableList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Postavljanje RecyclerView-a
        binding.rvDrivers.apply {
            adapter = DriverAdapter(requireContext(), drivers)
            layoutManager = LinearLayoutManager(requireContext())
        }

        setupSwipeToDelete()
    }

    // --- SEARCH LOGIKA ---
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        val filtered = if (query.isNullOrBlank()) {
            allDrivers
        } else {
            allDrivers.filter {
                it.lastName.contains(query, ignoreCase = true) ||
                        it.teamName.contains(query, ignoreCase = true)
            }
        }
        drivers.clear()
        drivers.addAll(filtered)
        binding.rvDrivers.adapter?.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> {
                showSortDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val options = arrayOf("Prezime (A-Z)", "Prezime (Z-A)", "Broj vozača")
        AlertDialog.Builder(requireContext())
            .setTitle("Sortiraj vozače")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> drivers.sortBy { it.lastName }
                    1 -> drivers.sortByDescending { it.lastName }
                    2 -> drivers.sortBy { it.driverNumber }
                }
                binding.rvDrivers.adapter?.notifyDataSetChanged()
            }
            .show()
    }

    // --- SWIPE TO DELETE LOGIKA ---
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

                // Brisanje iz baze
                requireContext().contentResolver.delete(
                    ContentUris.withAppendedId(F1_PROVIDER_CONTENT_URI, driverToDelete._id!!),
                    null,
                    null
                )

                // Sinkronizacija lista
                allDrivers.remove(driverToDelete)
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
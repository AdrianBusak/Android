package hr.algebra.f1_app.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.f1_app.DRIVER_POS
import hr.algebra.f1_app.DriverPagerActivity
import hr.algebra.f1_app.R
import hr.algebra.f1_app.databinding.ItemDriverBinding
import hr.algebra.f1_app.framework.startActivity
import hr.algebra.f1_app.model.F1Driver
import hr.algebra.f1_app.provider.F1_PROVIDER_CONTENT_URI
import java.io.File

class DriverAdapter(
    private val context: Context,
    private val drivers: MutableList<F1Driver>
) : RecyclerView.Adapter<DriverAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDriverBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val driver = drivers[position]

        holder.binding.tvDriverName.text = "${driver.firstName} ${driver.lastName}"

        holder.binding.ivFavorite.setImageResource(
            if (driver.favorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
        )

        holder.itemView.setOnClickListener {
            context.startActivity<DriverPagerActivity>(DRIVER_POS, position)
        }

        holder.itemView.setOnLongClickListener {
            deleteDriver(position)
            true
        }

        holder.binding.ivFavorite.setOnClickListener {
            driver.favorite = !driver.favorite

            val values = ContentValues().apply {
                put(F1Driver::favorite.name, if (driver.favorite) 1 else 0)
            }

            context.contentResolver.update(
                ContentUris.withAppendedId(F1_PROVIDER_CONTENT_URI, driver._id!!),
                values, null, null
            )

            holder.binding.ivFavorite.setImageResource(
                if (driver.favorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
            )
        }

        if (driver.headshotPath.isNotEmpty()) {
            Picasso.get()
                .load(File(driver.headshotPath))
                .error(R.drawable.f1_default_driver)
                .into(holder.binding.ivDriver)
        } else {
            holder.binding.ivDriver.setImageResource(R.drawable.f1_default_driver)
        }
    }

    private fun deleteDriver(position: Int) {
        val driver = drivers[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(F1_PROVIDER_CONTENT_URI, driver._id!!),
            null,
            null
        )
        if (driver.headshotPath.isNotEmpty()) {
            File(driver.headshotPath).delete()
        }
        drivers.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount() = drivers.size

    class ViewHolder(val binding: ItemDriverBinding) : RecyclerView.ViewHolder(binding.root)
}
package hr.algebra.f1_app.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import hr.algebra.f1_app.DriverPagerActivity
import hr.algebra.f1_app.R
import hr.algebra.f1_app.framework.startActivity
import hr.algebra.f1_app.model.F1Driver
import hr.algebra.f1_app.provider.F1_PROVIDER_CONTENT_URI
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class DriverAdapter(
    private val context: Context,
    private val drivers: MutableList<F1Driver>
) : RecyclerView.Adapter<DriverAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_driver, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val driver = drivers[position]
        holder.bind(driver)

        holder.itemView.setOnClickListener {
            context.startActivity<DriverPagerActivity>(
                DRIVER_POS,
                position
            )
        }

        holder.itemView.setOnLongClickListener {
            deleteDriver(position)
            true
        }
    }

    private fun deleteDriver(position: Int) {
        val driver = drivers[position]
        context.contentResolver.delete(
            Uri.withAppendedPath(F1_PROVIDER_CONTENT_URI, driver._id.toString()),
            null,
            null
        )
        File(driver.headshotPath!!).delete()
        drivers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, drivers.size)
    }

    override fun getItemCount() = drivers.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDriverName: TextView = itemView.findViewById(R.id.tvDriverName)
        private val tvDriverTeam: TextView = itemView.findViewById(R.id.tvDriverTeam)
        private val ivDriver: ImageView = itemView.findViewById(R.id.ivDriver)

        fun bind(driver: F1Driver) {
            tvDriverName.text = "${driver.firstName} ${driver.lastName}"
            tvDriverTeam.text = driver.teamName ?: "Unknown Team"

            // ✅ SAFE NULL CHECK + DEBUG
            val headshotPath = driver.headshotPath
            Log.d("DriverAdapter", "${driver.firstName}: headshotPath='$headshotPath'")

            if (!headshotPath.isNullOrEmpty()) {
                val photoFile = File(headshotPath)
                Log.d("DriverAdapter", "${driver.firstName}: file exists=${photoFile.exists()}, size=${photoFile.length()}")

                if (photoFile.exists() && photoFile.length() > 0) {
                    Picasso.get()
                        .load(photoFile)
                        .placeholder(R.drawable.f1_default_driver)
                        .error(R.drawable.f1_default_driver)
                        .transform(RoundedCornersTransformation(30, 5))
                        .into(ivDriver)
                } else {
                    // File ne postoji → default
                    Picasso.get()
                        .load(R.drawable.f1_default_driver)
                        .into(ivDriver)
                }
            } else {
                // NULL path → default
                Log.w("DriverAdapter", "❌ NULL headshotPath za ${driver.firstName}")
                Picasso.get()
                    .load(R.drawable.f1_default_driver)
                    .into(ivDriver)
            }
        }

    }

    companion object {
        const val DRIVER_POS = "DRIVER_POS"
    }
}

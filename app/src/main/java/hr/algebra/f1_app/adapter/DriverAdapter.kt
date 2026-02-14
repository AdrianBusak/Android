package hr.algebra.f1_app.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.f1_app.DRIVER_POS
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_driver, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
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
            ContentUris.withAppendedId(F1_PROVIDER_CONTENT_URI, driver._id!!),
            null,
            null
        )
        File(driver.headshotPath!!).delete()
        drivers.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = drivers.count()

    // DriverAdapter.kt
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivDriver)
        private val tvItem = itemView.findViewById<TextView>(R.id.tvDriverName)

        fun bind(driver: F1Driver) {
            // Pazi: tvItem mora postojati u item_driver.xml
            tvItem.text = "${driver.firstName} ${driver.lastName}"

            // Picasso dio
            if (driver.headshotPath.isNotEmpty()) {
                Picasso.get()
                    .load(File(driver.headshotPath))
                    .error(R.drawable.f1_default_driver)
                    .into(ivItem)
            } else {
                ivItem.setImageResource(R.drawable.f1_default_driver)
            }
        }
    }
}
package hr.algebra.f1_app.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.f1_app.provider.F1_PROVIDER_CONTENT_URI
import hr.algebra.f1_app.R
import hr.algebra.f1_app.model.F1Driver
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class DriverPagerAdapter(
    private val context: Context,
    private val drivers: MutableList<F1Driver>
) : RecyclerView.Adapter<DriverPagerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_driver_pager, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val driver = drivers[position]
        holder.bind(driver)

        holder.ivFavorite.setOnClickListener {
            updateDriver(position)
        }
    }

    // "content://hr.algebra.f1_app.provider/drivers
    //"content://hr.algebra.f1_app.provider/drivers/22

    private fun updateDriver(position: Int) {
        val driver = drivers[position]
        driver.favorite = !driver.favorite
        context.contentResolver.update(
            ContentUris.withAppendedId(F1_PROVIDER_CONTENT_URI, driver._id!!),
            ContentValues().apply {
                put(F1Driver::favorite.name, driver.favorite)
            },
            null,
            null
        )

        notifyItemChanged(position)
    }

    override fun getItemCount() = drivers.count()

    // DriverPagerAdapter.kt
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDriver = itemView.findViewById<ImageView>(R.id.ivDriver)
        private val tvDriverNumber = itemView.findViewById<TextView>(R.id.tvDriverNumber)
        private val tvDriverName = itemView.findViewById<TextView>(R.id.tvDriverName)
        private val tvDriverTeam = itemView.findViewById<TextView>(R.id.tvDriverTeam)
        private val tvCountry = itemView.findViewById<TextView>(R.id.tvCountry)
        val ivFavorite = itemView.findViewById<ImageView>(R.id.ivFavorite)

        fun bind(driver: F1Driver) {
            tvDriverNumber.text = driver.driverNumber.toString()
            tvDriverName.text = "${driver.firstName} ${driver.lastName}"
            tvDriverTeam.text = driver.teamName
            tvCountry.text = driver.countryCode

            ivFavorite.setImageResource(
                if (driver.favorite) R.drawable.ic_favorite_on
                else R.drawable.ic_favorite_off
            )

            if (driver.headshotPath.isNotEmpty()) {
                Picasso.get()
                    .load(File(driver.headshotPath))
                    .error(R.drawable.f1_default_driver)
                    .into(ivDriver)
            }
        }
    }
}
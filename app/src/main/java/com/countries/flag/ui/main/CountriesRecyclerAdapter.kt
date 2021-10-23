package com.countries.flag.ui.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.countries.flag.R
import com.countries.flag.models.Country
import com.countries.flag.ui.util.MainUtil

class CountriesRecyclerAdapter(private val viewModel: MainActivityViewModel) :
    ListAdapter<Country, CountriesRecyclerAdapter.CustomHolder>(callback) {
    companion object {
        val callback = object : DiffUtil.ItemCallback<Country>() {
            override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean =
                oldItem.name == newItem.name


        }
    }

    inner class CustomHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val countryImagePreview: ImageView = v.findViewById(R.id.countryImagePreview)
        val countryNameView: TextView = v.findViewById(R.id.countryNameView)
        val alpha2CodeView: TextView = v.findViewById(R.id.alpha2CodeView)


        init {
            v.setOnClickListener {
                viewModel.showCountryDetails(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        return CustomHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.country_recycler_box, parent, false)
        )
    }


    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        val country = getItem(position)
        val context = holder.v.context
        val imageSource = MainUtil.getFlagsLink(country.alpha2Code)

        holder.countryNameView.text = country.name
        holder.alpha2CodeView.text = country.alpha2Code
        MainUtil.loadImageUrl(context, Uri.parse(imageSource), holder.countryImagePreview)

    }
}

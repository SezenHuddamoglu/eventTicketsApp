package com.sezen.eventticketsapp.citySelection

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sezen.eventticketsapp.R


class CityAdapter(
    private val cityList: List<String>,
    private var selectedCity: String?,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private val filteredCityList = mutableListOf<String>()

    init {
        filteredCityList.addAll(cityList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = filteredCityList[position]
        holder.bind(city)


    }

    override fun getItemCount(): Int {
        return filteredCityList.size
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityNameTextView: TextView = itemView.findViewById(R.id.cityNameTextView)

        fun bind(city: String) {
            cityNameTextView.text = city

            // Clicking to one city item
            itemView.setOnClickListener {
                onItemClick(city)
            }

            //I change the background color of city items when user clicking on them
            if (selectedCity != null && selectedCity == city) {
                itemView.setBackgroundColor(Color.rgb(93, 161, 148))
            } else {
                itemView.setBackgroundColor(Color.rgb(211, 229, 193))
            }
        }
    }

    //When user change the city selectedCity variable will update
    fun setSelectedCity(city: String) {
        selectedCity = city
        notifyDataSetChanged()
    }

    //A function for filtering search results. I used lowercase() to avoid case sensitivity
    fun filter(query: CharSequence?) {
        filteredCityList.clear()
        if (query.isNullOrEmpty()) {
            filteredCityList.addAll(cityList)
        } else {
            val filterPattern = query.toString().lowercase().trim()
            cityList.forEach { city ->
                if (city.lowercase().contains(filterPattern.lowercase())) {
                    filteredCityList.add(city)
                }
            }
        }
        notifyDataSetChanged()
    }

}


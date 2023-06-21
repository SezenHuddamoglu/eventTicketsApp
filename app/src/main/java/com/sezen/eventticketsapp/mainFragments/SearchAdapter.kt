package com.sezen.eventticketsapp.mainFragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sezen.eventticketsapp.R
import com.sezen.eventticketsapp.network.Event

class SearchAdapter (private var eventList: List<Event>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    // Function to update the data in the adapter
    fun setData(events: List<Event>) {
        eventList = events
        notifyDataSetChanged()
    }


    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            // Set click listener for each item in the RecyclerView
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = eventList[position]
                    val context = itemView.context

                    // Start EventDetail activity and pass the event ID as an extra
                    val intent = Intent(context, EventDetail::class.java)
                    intent.putExtra("eventId", event.id)
                    context.startActivity(intent)
                }
            }
        }
        fun bindData(event: Event) {
            // Bind the event data to the views in the ViewHolder
            val name = itemView.findViewById<TextView>(R.id.eventNameTextView)
            name.text = event.name

            val date=itemView.findViewById<TextView>(R.id.eventDateTextView)
            date.text=event.dates?.start?.localDate

            val time=itemView.findViewById<TextView>(R.id.eventTimeTextView)
            time.text = event.dates?.start?.localTime

            val place=itemView.findViewById<TextView>(R.id.eventPlaceTextView)
            place.text = event._embedded?.venues?.get(0)?.name ?: null

            // Load the event image with using Coil
            val firstImage = if (event.images.isNotEmpty()) event.images[0] else null

            if (firstImage != null) {
                val imageView = itemView.findViewById<ImageView>(R.id.eventPosterImageView)

                imageView.load(firstImage.url) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder_image)
                    error(R.drawable.error_image)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        // Bind the data to the ViewHolder
        holder.bindData(eventList[position])
    }
}
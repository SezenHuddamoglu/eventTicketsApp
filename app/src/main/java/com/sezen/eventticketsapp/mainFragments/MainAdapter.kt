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


class MainAdapter(private var eventList: List<Event>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    // Update the data in the adapter
    fun setData(events: List<Event>) {
        eventList = events
        notifyDataSetChanged()
    }


    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            // Set a click listener on the item view
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = eventList[position]
                    val context = itemView.context

                    // Start the EventDetail activity with the selected event's ID
                    val intent = Intent(context, EventDetail::class.java)
                    intent.putExtra("eventId", event.id)
                    context.startActivity(intent)
                }
            }
        }
        // Bind data to the view holder
        fun bindData(event: Event) {
            val name = itemView.findViewById<TextView>(R.id.name)
            name.text = event.name

            val firstImage = if (event.images.isNotEmpty()) event.images[0] else null

            if (firstImage != null) {
                val imageView = itemView.findViewById<ImageView>(R.id.image)

                // Load the image using Coil
                imageView.load(firstImage.url) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder_image)
                    error(R.drawable.error_image)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        // Inflate the layout for each item in the recycler view
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return MainViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        // Return the number of items in the data list
        return eventList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        // Bind data to the view holder
        holder.bindData(eventList[position])
    }
}

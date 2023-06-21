package com.sezen.eventticketsapp.mainFragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sezen.eventticketsapp.R

class PreviousTicketsAdapter(private val userList: ArrayList<Ticket>) :
    RecyclerView.Adapter<PreviousTicketsAdapter.MyViewHolder>() {

    // Create the ViewHolder for the RecyclerView item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // Inflate the layout for the ticket item
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.ticket_item,
            parent, false
        )
        return MyViewHolder(itemView)

    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Get the current ticket item
        val currentitem = userList[position]
        // Set the event name, event date, and purchase date in the corresponding TextViews
        holder.eventName.text = currentitem.eventName
        holder.eventDate.text = currentitem.eventDate
        holder.purchaseDate.text = currentitem.buyingDate

    }

    override fun getItemCount(): Int {
        // Return the total number of items in the RecyclerView
        return userList.size
    }

    // ViewHolder class for the RecyclerView item
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize the TextViews
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventDate: TextView = itemView.findViewById(R.id.eventDate)
        val purchaseDate: TextView = itemView.findViewById(R.id.purchaseDate)

    }
}
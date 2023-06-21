package com.sezen.eventticketsapp.mainFragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SelectTicketAdapter(
    private val context: Context,
    private val tickets: MutableList<SelectedTicket>
) : ArrayAdapter<SelectedTicket>(context, 0, tickets) {

    // This method is responsible for creating and returning the View for each item in the list
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            // If convertView is null, inflate the layout for the item view
            itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        }

        // Get the SelectedTicket object at the given position
        val ticket = tickets[position]

        // Create the text to be displayed in the item view
        val ticketText = "${ticket.quantity} ticket ${ticket.category.name} - ${ticket.category.price * ticket.quantity} $"

        // Set the text in the TextView of the item view
        itemView?.findViewById<TextView>(android.R.id.text1)?.text = ticketText

        return itemView!!
    }

    override fun getCount(): Int {
        return tickets.size
    }

    // Add a ticket to the adapter's ticket list and notify the adapter of the data change
    fun addTicket(ticket: SelectedTicket) {
        tickets.add(ticket)
        notifyDataSetChanged()
    }

}

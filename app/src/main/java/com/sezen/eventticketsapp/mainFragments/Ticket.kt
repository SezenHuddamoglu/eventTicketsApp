package com.sezen.eventticketsapp.mainFragments

import java.io.Serializable

data class TicketCategory(val name: String, val price: Double) {
    override fun toString(): String {
        return name +"  -   " +price
    }
}

data class SelectedTicket(val category: TicketCategory, val quantity: Int) : Serializable

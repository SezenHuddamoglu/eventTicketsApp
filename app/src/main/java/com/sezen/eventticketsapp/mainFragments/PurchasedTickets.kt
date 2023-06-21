package com.sezen.eventticketsapp.mainFragments

data class PurchasedTicket(
    val ticketId: String,
    var userId: String,
    val ticketName: String,
    val ticketPrice: Double
)



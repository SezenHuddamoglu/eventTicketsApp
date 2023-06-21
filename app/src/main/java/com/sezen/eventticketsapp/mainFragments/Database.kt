package com.sezen.eventticketsapp.mainFragments

//Data classes that I used for database
data class Ticket(
    val eventName: String = "",
    val eventDate: String = "",
    val buyingDate: String = "",
    val cancelled: Boolean = false,
    val userId: String = ""
)

data class User(
    val id: String = "",
    val tickets: List<Ticket> = listOf()
)



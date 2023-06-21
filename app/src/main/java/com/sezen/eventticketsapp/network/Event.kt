package com.sezen.eventticketsapp.network

import android.provider.MediaStore
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class EventResponse(
    val _embedded: Embedded?
)

data class Embedded(
    val events: List<Event>
)

data class Event(
    val name: String,
    val images: List<Image>,
    val id: String,
    val _embedded: EventEmbedded?,
    val classifications: List<Classification>?,
    val dates: Dates?,
    val priceRanges: List<PriceRange>?,
    val seatmap: Seatmap?,
    val promoter :Promoter?
)

data class EventEmbedded(
    val venues: List<Venue>?
)
data class Image(
    val ratio: String?,
    val url: String?,
    val width: Int?,
    val height: Int?,
    val fallback: Boolean?
)
data class Venue(
    val name: String?,
    val type: String?,
    val id: String?,
    val test: Boolean?,
    val url: String?,
    val locale: String?,
    val aliases: List<String>?,
    val images: List<Image>?,
    val postalCode: String?,
    val timezone: String?,
    val city: City,
    val state: State?,
    val country: Country?,
    val address: Address?,
    val location: Location?,
    val markets: List<Market>?,
    val dmas: List<DMA>?,
    val social: Social?,
    val boxOfficeInfo: BoxOfficeInfo?,
    val parkingDetail: String?,
    val accessibleSeatingDetail: String?,
    val generalInfo: GeneralInfo?,
   // val upcomingEvents: UpcomingEvents,
    val ada: ADA?,
    val _links: Links?
)

data class City(
    val name: String?
)

data class State(
    val name: String?,
    val stateCode: String?
)

data class Country(
    val name: String?,
    val countryCode: String?
)

data class Address(
    val line1: String?
)

data class Location(
    val longitude: String?,
    val latitude: String?
)

data class Market(
    val name: String?,
    val id: String?
)

data class DMA(
    val id: Int?
)

data class Social(
    val twitter: Twitter?
)

data class Twitter(
    val handle: String?
)

data class BoxOfficeInfo(
    val phoneNumberDetail: String?,
    val openHoursDetail: String?,
    val acceptedPaymentDetail: String?,
    val willCallDetail: String?
)
data class Promoter(
    val name: String?,
    val description: String?,
    val id: String?
)

data class GeneralInfo(
    val generalRule: String?,
    val childRule: String?
)


data class ADA(
    val adaPhones: String?,
    val adaCustomCopy: String?,
    val adaHours: String?
)

data class Links(
    val self: Self?
)

data class Self(
    val href: String?
)

data class Classification(
    val primary: Boolean?,
    val segment: Segment?,
    val genre: Genre?,
    val subGenre: SubGenre?,
    val type: Type?,
    val subType: SubType?,
    val family: Boolean?
)
data class Segment(
    val id: String?,
    val name: String?
)

data class Genre(
    val id: String?,
    val name: String?
)

data class SubGenre(
    val id: String?,
    val name: String?
)

data class Type(
    val id: String?,
    val name: String?
)

data class SubType(
    val id: String?,
    val name: String?
)
data class Dates(
    val start: Start?,
    val timezone: String?,
    val status: Status?,
    val spanMultipleDays: Boolean?
)

data class Start(
    val localDate: String?,
    val localTime: String?,
    val dateTime: String?,
    val dateTBD: Boolean?,
    val dateTBA: Boolean?,
    val timeTBA: Boolean?,
    val noSpecificTime: Boolean?
)

data class Status(
    val code: String?
)
data class PriceRange(
    val type: String?,
    val currency: String?,
    val min: Double?,
    val max: Double?
)
data class Seatmap(
    val staticUrl: String?
)

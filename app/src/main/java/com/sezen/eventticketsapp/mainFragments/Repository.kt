package com.sezen.eventticketsapp.mainFragments


import com.sezen.eventticketsapp.network.ApiService
import com.sezen.eventticketsapp.network.Event

class Repository(private val apiService: ApiService) {
    // Fetch a list of events based on the provided parameters
      suspend fun getEvents(
          city: String,
          segment: String,
          pageCount: Int,
          limit: Int
      ): List<Event> {
          val distinctEvents = mutableListOf<Event>()
          val eventNames = mutableSetOf<String>()

          var totalEvents = 0
          var currentPage = 1

        // Retrieve events until the total number of events reaches the limit or the page count is exceeded
          while (totalEvents < limit && currentPage <= pageCount) {
              val response = apiService.getEvents(currentPage,city,segment)

              if (response.isSuccessful) {
                  val eventResponse = response.body()
                  if (eventResponse?._embedded?.events != null) {
                      val events = eventResponse._embedded.events

                      events?.forEach { event ->
                          if (totalEvents >= limit) {
                              return@forEach
                          }

                          // Add distinct events to the list and keep track of their names
                          if (!eventNames.contains(event.name)) {
                              distinctEvents.add(event)
                              eventNames.add(event.name)
                              totalEvents++
                          }
                      }
                  } else {
                      throw Exception("API request failed: ${response.code()} ${response.message()}")
                  }
              }

              currentPage++
          }

          return distinctEvents
      }

    // Fetch details of a specific event based on the provided event ID
      suspend fun getEventDetails(eventId: String): Event {
          val response = apiService.getEventDetails(eventId)

          if (response.isSuccessful) {
              val eventResponse = response.body()
              return eventResponse ?: throw Exception("Event not found")
          } else {
              throw Exception("API request failed: ${response.code()} ${response.message()}")
          }
      }

    // Search events based on a keyword
      suspend fun searchEvents(keyword: String): List<Event>? {
          val response = apiService.searchEvents(keyword)

          if (response.isSuccessful) {
              val eventResponse = response.body()
              return eventResponse?._embedded?.events
          } else {
              throw Exception("API request failed: ${response.code()} ${response.message()}")
          }
      }

  }


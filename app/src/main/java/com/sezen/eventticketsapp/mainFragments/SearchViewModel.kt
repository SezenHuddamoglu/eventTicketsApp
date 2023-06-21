package com.sezen.eventticketsapp.mainFragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sezen.eventticketsapp.network.ApiClient
import com.sezen.eventticketsapp.network.Event
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SearchViewModel(private val repository: Repository = Repository(ApiClient.apiService)) : ViewModel() {
    // LiveData for search results
    private val _searchResults = MutableLiveData<ScreenState<List<Event>?>>()
    val searchResults: LiveData<ScreenState<List<Event>?>>
        get() = _searchResults

    // Search events based on a keyword
    fun searchEvents(keyword: String) {
        viewModelScope.launch {
            try {
                // Call the repository to search events
                val events = repository.searchEvents(keyword)
                // Update the searchResults LiveData with the success state and the retrieved events
                _searchResults.value = ScreenState.Success(events)

            } catch (e: Exception) {
                val errorMessage = if (e is HttpException) {
                    "${e.code()} and ${e.message()}"
                } else {
                    Log.e("API Error", e.toString(), e)
                    "API FAILED"
                }
                // Update the searchResults LiveData with the error state and the error message
                _searchResults.value = ScreenState.Error(errorMessage)
            }
        }
    }
    // Functions for sorting the search results
    fun sortByAlphabetical() {
        val sortedList = searchResults.value?.data?.sortedBy { it.name }
        _searchResults.value = ScreenState.Success(sortedList)
    }

    fun sortByDateAscending() {
        val sortedList = searchResults.value?.data?.sortedBy { it.dates?.start?.localDate }
        _searchResults.value = ScreenState.Success(sortedList)
    }

    fun sortByDateDescending() {
        val sortedList = searchResults.value?.data?.sortedByDescending { it.dates?.start?.localDate }
        _searchResults.value = ScreenState.Success(sortedList)
    }

    fun sortByTimeAscending() {
        val sortedList = searchResults.value?.data?.sortedBy { it.dates?.start?.localTime }
        _searchResults.value = ScreenState.Success(sortedList)
    }

    fun sortByTimeDescending() {
        val sortedList = searchResults.value?.data?.sortedByDescending { it.dates?.start?.localTime }
        _searchResults.value = ScreenState.Success(sortedList)
    }

    fun sortByPriceAscending() {
        val sortedList = searchResults.value?.data?.sortedBy { it.priceRanges?.get(0)?.min}
        _searchResults.value = ScreenState.Success(sortedList)
    }

    fun sortByPriceDescending() {
        val sortedList = searchResults.value?.data?.sortedByDescending { it.priceRanges?.get(0)?.min}
        _searchResults.value = ScreenState.Success(sortedList)
    }

    // Functions for filtering the search results
    fun filterByTimeRange(timeFrom: String, timeTo: String) {
        val filteredList = searchResults.value?.data?.filter { event ->
            val eventTime = event.dates?.start?.localTime
            eventTime!! in timeFrom..timeTo
        }

        _searchResults.value = ScreenState.Success(filteredList)
    }

    fun filterByDateRange(dateFrom: String, dateTo: String) {
        val filteredList = searchResults.value?.data?.filter { event ->
            val eventDate = event.dates?.start?.localDate
            eventDate!! in dateFrom..dateTo
        }

        _searchResults.value = ScreenState.Success(filteredList)
    }

    fun filterByPriceRange(priceFrom: Double, priceTo: Double) {
        val filteredList = searchResults.value?.data?.filter { event ->
            val eventPrice =  event.priceRanges?.get(0)?.min
            eventPrice!! in priceFrom..priceTo
        }

        _searchResults.value = ScreenState.Success(filteredList)
    }


}


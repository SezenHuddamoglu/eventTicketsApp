package com.sezen.eventticketsapp.mainFragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sezen.eventticketsapp.network.ApiClient
import com.sezen.eventticketsapp.network.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: Repository = Repository(ApiClient.apiService)): ViewModel() {

    // LiveData for music events
    private var _musicEventsLiveData = MutableLiveData<ScreenState<List<Event>?>>()
    val musicEventsLiveData: LiveData<ScreenState<List<Event>?>>
        get() = _musicEventsLiveData

    // LiveData for sports events
    private var _sportsEventsLiveData = MutableLiveData<ScreenState<List<Event>?>>()
    val sportsEventsLiveData: LiveData<ScreenState<List<Event>?>>
        get() = _sportsEventsLiveData

    // LiveData for arts events
    private var _artsEventsLiveData = MutableLiveData<ScreenState<List<Event>?>>()
    val artsEventsLiveData: LiveData<ScreenState<List<Event>?>>
        get() = _artsEventsLiveData

    // Selected city for event retrieval
    private var selectedCity: String? = "Los Angeles"


   init {
       selectedCity?.let { setCity(it) }
        viewModelScope.launch {
            try {
                // Fetch events for the categories
                selectedCity?.let { fetchEventsByCategory(it, "Music", 5, 15) }

                selectedCity?.let { fetchEventsByCategory(it, "Sports", 5, 15) }

                selectedCity?.let { fetchEventsByCategory(it, "Arts & Theatre", 5, 15) }
            } catch (e: Exception) {
                val errorMessage = if (e is HttpException) {
                    "${e.code()} and ${e.message()}"
                } else {
                    Log.e("API Error", e.toString(), e)
                    "API FAILED"
                }
                // Post error state for events
                _musicEventsLiveData.postValue(ScreenState.Error(errorMessage, null))
                _sportsEventsLiveData.postValue(ScreenState.Error(errorMessage, null))
                _artsEventsLiveData.postValue(ScreenState.Error(errorMessage, null))
            }
        }
    }


    private suspend fun fetchEventsByCategory(
        city: String,
        segment: String,
        pageCount: Int,
        limit: Int
    ) {
        // Fetch events from the repository
        val events = repository.getEvents(city, segment, pageCount, limit)
        when (segment) {
            // Post the events to the corresponding LiveData based on the segment/category
            "Music" -> _musicEventsLiveData.postValue(ScreenState.Success(events))
            "Sports" -> _sportsEventsLiveData.postValue(ScreenState.Success(events))
            "Arts & Theatre" -> _artsEventsLiveData.postValue(ScreenState.Success(events))
        }
    }

    // Set the selected city for event retrieval
    fun setCity(city: String) {
        selectedCity = city

    }

}
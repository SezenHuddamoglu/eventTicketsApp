package com.sezen.eventticketsapp.mainFragments

sealed class ScreenState<T>(val data : T? =null, val message :String? = null) {

    // Represents a successful state with the provided data
    class Success<T>(data: T): ScreenState<T>(data)

    // Represents a loading state with optional data
    class Loading<T>(data: T? =null): ScreenState<T>(data)

    // Represents an error state with the provided error message and optional data
    class Error<T>(message:String, data:T?=null):ScreenState<T>(data,message)
}
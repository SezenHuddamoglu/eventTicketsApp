package com.sezen.eventticketsapp.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

import retrofit2.http.Path

object ApiClient {
    private const val BASE_URL = "https://app.ticketmaster.com/discovery/v2/"
    private const val API_KEY = "7J14RJzmyMVoKf8jkJApjUuQxAG7rYj8"
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()



    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .url(originalRequest.url.newBuilder()
                    .addQueryParameter("apikey", API_KEY)
                    .build())
                .build()
            chain.proceed(modifiedRequest)
        }
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService {
     @GET("events")
     suspend fun getEvents(
         @Query("page") page: Int ,
         @Query("city") city: String,
         @Query("segmentName") segment:String
     ): Response<EventResponse>

    @GET("events/{eventId}")
    suspend fun getEventDetails(
        @Path("eventId") eventId: String
    ): Response<Event>

    @GET("events")
    suspend fun searchEvents(
        @Query("keyword") keyword: String
    ): Response<EventResponse>

}

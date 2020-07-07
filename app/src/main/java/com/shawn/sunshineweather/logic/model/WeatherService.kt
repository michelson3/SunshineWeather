package com.shawn.sunshineweather.logic.model

import com.shawn.sunshineweather.AppApplication
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.5/${AppApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    @GET("v2.5/${AppApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<DailyResponse>
}
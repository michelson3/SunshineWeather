package com.shawn.sunshineweather.logic.network

import com.shawn.sunshineweather.logic.model.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object NetworkUtil {
    // 搜索地点接口
    private val placeService = ServiceCreator.create(PlaceService::class.java)

    // 获取天气接口
    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    /**
     * 搜索地点
     */
    suspend fun searchPlace(query: String) = placeService.searchPlaces(query).await()

    /**
     * 获取实时天气
     */
    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    /**
     * 获取未来几天的天气
     */
    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("Response body is null"))
                    }
                }

            })
        }
    }
}
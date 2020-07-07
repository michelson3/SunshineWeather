package com.shawn.sunshineweather.logic

import androidx.lifecycle.liveData
import com.shawn.sunshineweather.logic.model.Place
import com.shawn.sunshineweather.logic.model.Weather
import com.shawn.sunshineweather.logic.network.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object Repository {
    /**
     * 搜索地点
     */
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = NetworkUtil.searchPlace(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<com.shawn.sunshineweather.logic.model.Place>>(e)
        }
        emit(result as Result<List<Place>>)
    }

    /**
     * 搜索地点
     */
    fun refreshWeather(lng: String, lat: String) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
                val deferredRealtime = async { NetworkUtil.getRealtimeWeather(lng, lat) }
                val deferredDaily = async { NetworkUtil.getDailyWeather(lng, lat) }

                val realTimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if (realTimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather = Weather(
                        realTimeResponse.result.realtime,
                        dailyResponse.result.daily
                    )
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realTimeResponse.status}" +
                                    "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }

        } catch (e: Exception) {
            Result.failure<Weather>(e)
        }
        emit(result as Result<Weather>)
    }
}
package com.shawn.sunshineweather.logic

import androidx.lifecycle.liveData
import com.shawn.sunshineweather.logic.dao.PlaceDao
import com.shawn.sunshineweather.logic.model.Place
import com.shawn.sunshineweather.logic.model.Weather
import com.shawn.sunshineweather.logic.network.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    /**
     * 搜索地点
     */
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = NetworkUtil.searchPlace(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    /**
     * 搜索地点
     */
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
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
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    //todo : getSharedPreferences 是耗时操作，建议放在子线程完成。
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}
package com.shawn.sunshineweather.logic

import androidx.lifecycle.liveData
import com.shawn.sunshineweather.logic.model.Place
import com.shawn.sunshineweather.logic.network.NetworkUtil
import kotlinx.coroutines.Dispatchers

object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = NetworkUtil.searchPlace(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e:Exception) {
            Result.failure<List<com.shawn.sunshineweather.logic.model.Place>>(e)
        }
        emit(result as Result<List<Place>>)
    }
}
package com.shawn.sunshineweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.shawn.sunshineweather.AppApplication
import com.shawn.sunshineweather.logic.model.Place

object PlaceDao {
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", null)
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() =
        AppApplication.context.getSharedPreferences("sunshine_weather", Context.MODE_PRIVATE)
}
package com.shawn.sunshineweather.logic.network

import com.shawn.sunshineweather.AppApplication
import com.shawn.sunshineweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    /**
     * 搜索城市
     */
    @GET("v2/place?token=${AppApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}
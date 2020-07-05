package com.shawn.sunshineweather

import android.app.Application
import android.content.Context

class AppApplication : Application() {
    companion object {
        const val TOKEN="2nTzssyjkfRJxTMK"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
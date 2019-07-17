package com.mcuadrada.pinbox.Utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class SharedApp: Application(){
    companion object{
        lateinit var prefs: SharedPreference
    }

    override fun onCreate() {
        super.onCreate()
        prefs = SharedPreference(applicationContext)
    }
}

class SharedPreference (context: Context) {
    private val PREFS_NAME = "com.mcuadrada.sharedpreferences"
    private val CURRENT_PIN = "current_pin"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var pin: String
        get() = prefs.getString(CURRENT_PIN, "")!!
        set(value) = prefs.edit().putString(CURRENT_PIN, value).apply()
}
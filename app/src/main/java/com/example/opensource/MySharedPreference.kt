package com.example.opensource

import android.content.Context

object MySharedPreference {

    private var TEMPERATURE = "TEMPERATURE"
    private var ICON = "ICON"

    fun setTemperature(context: Context, temperature: String) {
        val pref = context.getSharedPreferences(TEMPERATURE, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(TEMPERATURE, temperature)
        editor.apply()
    }

    fun getTemperature(context: Context): String {
        val pref = context.getSharedPreferences(TEMPERATURE, Context.MODE_PRIVATE)
        return pref.getString(TEMPERATURE, "0.0")!!
    }

    fun setIcon(context: Context, icon: Int) {
        val pref = context.getSharedPreferences(ICON, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(ICON, icon)
        editor.apply()
    }

    fun getIcon(context: Context): Int {
        val pref = context.getSharedPreferences(ICON, Context.MODE_PRIVATE)
        return pref.getInt(ICON, 0)
    }
}
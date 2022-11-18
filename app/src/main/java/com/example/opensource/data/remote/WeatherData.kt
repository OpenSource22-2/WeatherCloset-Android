package com.example.opensource.data.remote

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class WeatherData {
    lateinit var tempString: String
    lateinit var icon: String
    lateinit var weatherType: String
    private var weatherId: Int = 0
    private var tempInt: Int = 0

    fun fromJson(jsonObject: JSONObject?): WeatherData? {
        try {
            val weatherData = WeatherData()
            weatherData.weatherId =
                jsonObject?.getJSONArray("weather")?.getJSONObject(0)?.getInt("id")!!
            weatherData.weatherType =
                jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
            weatherData.icon = updateWeatherIcon(weatherData.weatherId)
            val roundedTemp: Int =
                (jsonObject.getJSONObject("main").getDouble("temp") - 273.15).toInt()
            weatherData.tempString = roundedTemp.toString()
            weatherData.tempInt = roundedTemp
            Log.d("WEATHER", "fromJson: $weatherData")
            return weatherData
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.d("WEATHER", "fromJson: fromJson Error")
            return null
        }
    }

    private fun updateWeatherIcon(condition: Int): String {
        when (condition) {
            in 200..299 -> {
                return "ic_11d"
            }
            in 300..499 -> {
                return "ic_09d"
            }
            in 500..599 -> {
                return "ic_10d"
            }
            in 600..700 -> {
                return "ic_13d"
            }
            in 701..771 -> {
                return "ic_50d"
            }
            in 772..799 -> {
                return "ic_50d"
            }
            800 -> {
                return "ic_01d"
            }
            801 -> {
                return "ic_02d"
            }
            802 -> {
                return "ic_03d"
            }
            803, 804 -> {
                return "ic_04d"
            }
            else -> return "unknown"
        }
    }
}

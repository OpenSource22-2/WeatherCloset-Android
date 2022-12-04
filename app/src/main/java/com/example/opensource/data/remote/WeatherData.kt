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
    lateinit var popUpText: String

    fun fromJson(jsonObject: JSONObject?): WeatherData? {
        try {
            val weatherData = WeatherData()
            weatherData.weatherId =
                jsonObject?.getJSONArray("weather")?.getJSONObject(0)?.getInt("id")!!
            weatherData.weatherType =
                jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
            weatherData.icon = updateWeatherIcon(weatherData)
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

    private fun updateWeatherIcon(weatherData: WeatherData): String {
        when (weatherData.weatherId) {
            in 200..299 -> {
                weatherData.popUpText = "번개 치는 날이에요!"
                return "ic_11d"
            }
            in 300..499 -> {
                weatherData.popUpText = "비가 많이 내려요. 장화를 신는 건 어떨까요?"
                return "ic_09d"
            }
            in 500..599 -> {
                weatherData.popUpText = "비가 많이 내려요. 장화를 신는 건 어떨까요?"
                return "ic_10d"
            }
            in 600..700 -> {
                weatherData.popUpText = "눈이 내려요. 장갑과 목도리는 필수:)"
                return "ic_13d"
            }
            in 701..771 -> {
                weatherData.popUpText = "바람이 많이 불어요. 겉옷을 챙기세요."
                return "ic_50d"
            }
            in 772..799 -> {
                weatherData.popUpText = "바람이 많이 불어요. 겉옷을 챙기세요."
                return "ic_50d"
            }
            800 -> {
                weatherData.popUpText = "날이 맑으니 산책하기 좋은 날이에요!"
                return "ic_01d"
            }
            801 -> {
                weatherData.popUpText = "구름이 조금 있어요. 밝은 옷을 입으시는 걸 추천해요!"
                return "ic_02d"
            }
            802 -> {
                weatherData.popUpText = "구름이 조금 있어요. 밝은 옷을 입으시는 걸 추천해요!"
                return "ic_03d"
            }
            803, 804 -> {
                weatherData.popUpText = "날이 흐려요. 밝은 옷을 입으시는 걸 추천해요!"
                return "ic_04d"
            }
            else -> {
                weatherData.popUpText = "날씨를 알 수 없어요. 날씨를 확인해주세요."
                return "unknown"
            }
        }
    }
}

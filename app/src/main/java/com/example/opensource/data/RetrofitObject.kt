package com.example.opensource.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

    private const val BASE_URL = "http://15.164.59.9:8080/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val provideWeatherClosetApi: WeatherClosetApi = retrofit.create(WeatherClosetApi::class.java)
}
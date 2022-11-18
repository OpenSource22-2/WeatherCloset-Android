package com.example.opensource.data

import com.example.opensource.Secret
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

    private const val BASE_URL = Secret.BASE_URL

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val provideWeatherClosetApi: WeatherClosetApi = retrofit.create(WeatherClosetApi::class.java)
}
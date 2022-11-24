package com.example.opensource.data

import com.example.opensource.Secret
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

    private const val BASE_URL = Secret.BASE_URL

    private val retrofit: Retrofit = Retrofit.Builder() // Retrofit 빌더 생성 (생성자 호출)
        .baseUrl(BASE_URL)  // 빌더 객체의 baseUrl 호출. 서버의 메인 URL 전달
        .addConverterFactory(GsonConverterFactory.create()) // gson 컨버터 연동
        .build()    // Retrofit 객체 반환

    // 인터페이스 객체를 create에 넘겨 실제 구현체 생성
    val provideWeatherClosetApi: WeatherClosetApi = retrofit.create(WeatherClosetApi::class.java)
}
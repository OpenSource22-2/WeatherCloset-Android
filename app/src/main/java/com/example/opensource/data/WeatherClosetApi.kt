package com.example.opensource.data

import com.example.opensource.data.remote.CreateRecordRequest
import com.example.opensource.data.remote.CreateRecordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface WeatherClosetApi {

    @Headers("Content-type:application/json")
    @POST("post")
    fun createRecord(
        @Body body: CreateRecordRequest
    ): Call<CreateRecordResponse>

}
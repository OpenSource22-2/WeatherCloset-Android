package com.example.opensource.data

import com.example.opensource.data.remote.CreateRecordRequest
import com.example.opensource.data.remote.CreateRecordResponse
import com.example.opensource.data.remote.HomeRecordResponse
import retrofit2.Call
import retrofit2.http.*

interface WeatherClosetApi {

    // 기록 등록
    @Headers("Content-type:application/json")
    @POST("record/{memberId}")
    fun createRecord(
        @Path("memberId") memberId: Int,
        @Body body: CreateRecordRequest
    ): Call<CreateRecordResponse>

    // 기록 조회
    @Headers("Content-type:application/json")
    @GET("record/{memberId}")
    fun getRecordList(
        @Path("memberId") memberId: Int
    ): Call<HomeRecordResponse>
}
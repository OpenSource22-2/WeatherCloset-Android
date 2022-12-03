package com.example.opensource.data

import com.example.opensource.data.remote.BaseResponse
import com.example.opensource.data.remote.CreateRecordRequest
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
    ): Call<BaseResponse>

    // 기록 전체 조회 (HOME FRAGMENT)
    @Headers("Content-type:application/json")
    @GET("member/{memberId}")
    fun getRecordList(
        @Path("memberId") memberId: Int
    ): Call<HomeRecordResponse>
}
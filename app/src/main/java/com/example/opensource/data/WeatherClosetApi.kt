package com.example.opensource.data

import com.example.opensource.data.remote.BaseResponse
import com.example.opensource.data.remote.CreateRecordRequest
import com.example.opensource.data.remote.HomeRecordResponse
import com.example.opensource.data.remote.RecordResponse
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

    // 기록 수정
    @Headers("Content-type:application/json")
    @PUT("record/{recordId}")
    fun updateRecord(
        @Path("recordId") recordId: Int,
        @Body body: CreateRecordRequest
    ): Call<BaseResponse>

    // 기록 단건 조회
    @Headers("Content-type:application/json")
    @GET("record/{recordId}")
    fun getRecord(
        @Path("recordId") recordId: Int
    ): Call<RecordResponse>

    // 기록 삭제
    @Headers("Content-type:application/json")
    @DELETE("record/{recordId}")
    fun deleteRecord(
        @Path("recordId") recordId: Int
    ): Call<BaseResponse>
}
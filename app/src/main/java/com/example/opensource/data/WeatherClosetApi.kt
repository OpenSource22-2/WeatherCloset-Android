package com.example.opensource.data

import com.example.opensource.data.remote.*
import retrofit2.Call
import retrofit2.http.*
import java.time.Month
import java.time.Year

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

    // 서치 기록 조회
    @Headers("Content-type:application/json")
    @GET("search")
    fun getSearchRecord(
        @Query("minTemperature") minTemperature: Double,
        @Query("maxTemperature") maxTemperature: Double
    ): Call<SearchRecordResponse>

    // 캘린더 기록 조회
    @Headers("Content-type:application/json")
    @GET("calendar/{memberId}")
    fun getCalendarRecord(
        @Path("memberId") memberId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<CalendarRecordResponse>


}
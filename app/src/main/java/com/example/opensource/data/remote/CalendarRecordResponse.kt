package com.example.opensource.data.remote

import com.google.gson.annotations.SerializedName

data class CalendarRecordResponse(
    val status: Int,
    val message: String,
    val data: Set<CalendarRecordData>
) {
    data class CalendarRecordData(
        val id: Int,
        val username: String,
        val imageUrl: String,
        val heart: Boolean,
        @SerializedName("date")
        val recordDate: String,
        val temperature: Double,
/*        val icon: Int,
        val stars: Int,
        val comment: String,*/
    )
}

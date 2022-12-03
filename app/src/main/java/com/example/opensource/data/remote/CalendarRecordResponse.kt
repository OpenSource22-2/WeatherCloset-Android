package com.example.opensource.data.remote

data class CalendarRecordResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<CalendarRecordData>
) {
    data class CalendarRecordData(
        val id: Int,
        val username: String,
        val imageUrl: String,
        val stars: Int,
        val comment: String,
        val heart: Boolean,
        val recordDate: String,
        val temperature: Double,
        val icon: Int
    )
}

package com.example.opensource.data.remote

data class SearchRecordResponse(
    val data: ArrayList<SearchRecordData>
) {
    data class SearchRecordData(
        val id: Int,
        val imageUrl: String,
        val heart: Boolean,
        val recordData: String,
        val temperature: Double
    )
}
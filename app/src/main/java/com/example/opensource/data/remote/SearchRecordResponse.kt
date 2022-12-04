package com.example.opensource.data.remote

import com.google.gson.annotations.SerializedName

data class SearchRecordResponse(
    val data: ArrayList<SearchRecordData>
) {
    data class SearchRecordData(
        val id: Int,
        val imageUrl: String,
        val heart: Boolean,
        @SerializedName("date")
        val recordData: String,
        val temperature: Double
    )
}
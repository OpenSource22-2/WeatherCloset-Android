package com.example.opensource.data.remote

import com.google.gson.annotations.SerializedName

data class HomeRecordResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<HomeRecordData>
) {
    data class HomeRecordData(
        val id: Int,
        val username: String,
        val imageUrl: String,
        val stars: Int,
        val comment: String,
        val heart: Boolean,
        @SerializedName("date")
        val recordDate: String,
        val temperature: Float,
        val icon: Int,
    )
}

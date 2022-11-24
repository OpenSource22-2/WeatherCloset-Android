package com.example.opensource.data.remote

data class HomeRecordResponse(
    val data: ArrayList<HomeRecordData>
) {
    data class HomeRecordData(
        val comment: String,
        val createdAt: String,
        val heart: Boolean,
        val id: Int,
        val imageUrl: String,
        val stars: Int,
        val temperature: Int,
        val username: String
    )
}

package com.example.opensource.data.remote

import com.google.gson.annotations.SerializedName

data class CreateRecordRequest(
    val comment: String,
    val heart: Boolean,
    val imageUrl: String,
    val stars: Int,
    @SerializedName("date")
    val recordDate: String,
    @SerializedName("tagIds")
    val tag: List<Long>,
)
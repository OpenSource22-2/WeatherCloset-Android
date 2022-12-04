package com.example.opensource.data.remote

import com.google.gson.annotations.SerializedName

data class CreateRecordRequest(
    val comment: String,
    val heart: Boolean,
    val imageUrl: String,
    val stars: Int,
    val recordDate: String,
    @SerializedName("tags")
    val tag: List<Long>,
)
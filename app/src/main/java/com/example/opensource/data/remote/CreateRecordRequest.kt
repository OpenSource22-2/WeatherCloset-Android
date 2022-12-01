package com.example.opensource.data.remote

data class CreateRecordRequest(
    val comment: String,
    val heart: Boolean,
    val imageUrl: String,
    val stars: Int
)
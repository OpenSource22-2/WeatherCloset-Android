package com.example.opensource.data.remote

import java.util.*

data class HomeRecordResponse(
    val imageUrl: String,
    val stars: Int,
    val comment: String,
    val createdAt: String,
    val heart: Boolean,
    val temperature: Int
)

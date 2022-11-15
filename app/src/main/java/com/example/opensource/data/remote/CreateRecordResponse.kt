package com.example.opensource.data.remote


data class CreateRecordResponse(
    val comment: String,
    val createdAt: String,
    val heart: Boolean,
    val imageUrl: String,
    val stars: Int,
    val temperature: Int
)
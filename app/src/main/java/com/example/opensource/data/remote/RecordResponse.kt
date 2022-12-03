package com.example.opensource.data.remote


data class RecordResponse(
    val message: String,
    val status: Int,
    val data: RecordData
)
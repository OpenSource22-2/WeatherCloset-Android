package com.example.opensource.data.remote

data class CreateRecordRequest(
    val comment: String,
    val heart: Boolean,
    val imageUrl: String,
    val stars: Int,
    val recordDate: String,
//    val tag: List<Long>,    // TODO: 태그 추가 기능 추가
)
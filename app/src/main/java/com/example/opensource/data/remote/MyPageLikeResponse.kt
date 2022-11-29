package com.example.opensource.data.remote

data class MyPageLikeResponse(
    val data: ArrayList<MyPageLikeData>
) {
    data class MyPageLikeData(
        val createdAt: String,
        val heart: Boolean,
        val id: Int,
        val imageUrl: String
    )
}

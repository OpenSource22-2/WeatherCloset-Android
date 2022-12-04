package com.example.opensource.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecordData(
    val id: Int,
    val username: String,
    val imageUrl: String,
    val stars: Int,
    val comment: String,
    val heart: Boolean,
    @SerializedName("date")
    val recordDate: String,
    var temperature: Double,
    var icon: Int,
    @SerializedName("tags")
    val tags: List<String>,
) : Parcelable
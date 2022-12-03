package com.example.opensource.data.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecordData(
    val id: Int,
    val username: String,
    val imageUrl: String,
    val stars: Int,
    val comment: String,
    val heart: Boolean,
    val recordDate: String,
    val temperature: Double,
    val icon: Int,
//    val tag: List<String>,    // TODO: tag
) : Parcelable
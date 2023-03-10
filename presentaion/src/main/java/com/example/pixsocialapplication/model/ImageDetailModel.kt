package com.example.pixsocialapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageDetailModel (
    val userName : String,
    val imageUrl : String,
    val createdAt : String,
): Parcelable
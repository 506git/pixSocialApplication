package com.example.pixsocialapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomUserInfoModel(
    val roomImage : String? = "",
    val userId : String? = "",
    val name : String? = "",
    val roomId : String? = ""
) : Parcelable

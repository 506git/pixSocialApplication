package com.example.data.model

import com.google.gson.annotations.SerializedName

data class RoomIdDTO(
    @SerializedName("roomId")
    private val roomId: String? = "",
)
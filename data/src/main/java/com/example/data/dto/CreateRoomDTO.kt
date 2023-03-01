package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class CreateRoomDTO (
    @SerializedName("room_name")
    private val roomName: String? = "",

    @SerializedName("member_ids")
    private val memberIds: List<String> = emptyList(),
)
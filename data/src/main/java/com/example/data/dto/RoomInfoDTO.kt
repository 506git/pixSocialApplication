package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class RoomInfoDTO(
    val _id: String? = "",

    val room_name: String? = "",

    val members: List<String>,

    val chats: List<ChatInfo?> = emptyList(),

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val memberCount: Int? = null,
)

data class ChatInfo(
    @SerializedName("user_id")
    val userId: String? = "",

    @SerializedName("message_body")
    val messageBody: String? = "",

    @SerializedName("message_type")
    val messageType: String? = "",
)


package com.example.data.dto

data class RoomListInfoDTO (
    val _id: String? = "",

    val room_name: String? = "",

    val members: List<String>,

    val chats: List<ChatInfo?> = emptyList(),

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val member_count: Int? = null,

    val room_image: String? = null,
)
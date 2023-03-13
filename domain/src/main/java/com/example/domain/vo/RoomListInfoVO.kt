package com.example.domain.vo

import com.example.domain.model.ChatInfo

data class RoomListInfoVO (
    val id: String? = "",

    val roomName: String? = "",

    val members: List<String>,

    val chats: List<ChatInfo?> = emptyList(),

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val memberCount: Int? = null,

    val roomImage: String? = null,
)

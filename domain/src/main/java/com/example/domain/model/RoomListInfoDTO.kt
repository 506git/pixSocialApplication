package com.example.domain.model


data class RoomListInfoDTO (
    val result: RoomListResultInfo,
)

data class RoomListResultInfo (
    val resultCode: String = "",

    val resultMessage: String = "",

    val content: List<RoomListInfo>,
)

data class RoomListInfo (
    val _id: String? = "",

    val room_name: String? = "",

    val members: List<String>,

    val chats: List<String?> = emptyList(),

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val member_Count: Int? = null,

    val room_image: String? = null,
)
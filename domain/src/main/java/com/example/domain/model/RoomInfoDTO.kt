package com.example.domain.model

data class RoomInfoDTO (
        private val result: RoomResultInfo,
)

data class RoomResultInfo (
        private val resultCode: String = "",

        private val resultMessage: String = "",

        private val content: RoomInfo2,
)

data class RoomInfo2 (
        val _id: String? = "",

        val room_name: String? = "",

        val members: List<String>,

        val chats: List<ChatInfo?> = emptyList(),

        val createdAt: String? = null,

        val updatedAt: String? = null,

        val memberCount: Int? = null,
)

data class ChatInfo (
        val user_id: String? = "",

        val messageBody: String? = "",

        val messageType: String? = "",
)


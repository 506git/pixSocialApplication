package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class RoomInfoDTO (
        @SerializedName("result")
        private val result: RoomResultInfo,
)

data class RoomResultInfo (
        @SerializedName("resultCode")
        private val resultCode: String = "",

        @SerializedName("resultMessage")
        private val resultMessage: String = "",

        @SerializedName("content")
        private val content: RoomInfo2,
)

data class RoomInfo2 (
        @SerializedName("_id")
        private val id: String? = "",

        @SerializedName("room_name")
        private val name: String? = "",

        @SerializedName("members")
        private val members: List<String>,

        @SerializedName("chats")
        private val chats: List<String?> = emptyList(),

        @SerializedName("createdAt")
        private val createdAt: String? = null,

        @SerializedName("updatedAt")
        private val updatedAt: String? = null,

        @SerializedName("memberCount")
        private val memberCount: Int? = null,
)


package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class RoomListInfoDTO (
    @SerializedName("result")
    private val result: RoomListResultInfo,
)

data class RoomListResultInfo (
    @SerializedName("resultCode")
    private val resultCode: String = "",

    @SerializedName("resultMessage")
    private val resultMessage: String = "",

    @SerializedName("content")
    private val content: List<RoomListInfo>,
)

data class RoomListInfo (
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

    @SerializedName("member_count")
    private val memberCount: Int? = null,

    @SerializedName("room_image")
    private val roomImage: String? = null,
)
package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class ChatListDTO(
    val user_id: String = "",

    val message_body: String = "",

    val message_type: String = "",

    val createdAt: String = ""
)
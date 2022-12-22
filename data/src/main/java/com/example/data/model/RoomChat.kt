package com.example.data.model

import java.time.LocalDateTime
import java.util.*

data class RoomChat(
    val message: String? = "",
    val message_id: String? = "",
    val message_type: String? = "",
    val message_date: String? = "",
    val message_sender_image: String? = "",
    val message_sender_name : String? = ""
)
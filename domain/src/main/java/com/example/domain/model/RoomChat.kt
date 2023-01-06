package com.example.domain.model

import java.time.LocalDateTime

data class RoomChat (
    val message: String? = "",
    val messageId: String? = "",
    val messageType: String? = "",
    val messageDate: String? = "",
    val messageSenderImage: String? = "",
    var messageSender : String = "you",
    val messageSenderName : String? = "",
    var messageKey : String = ""
)
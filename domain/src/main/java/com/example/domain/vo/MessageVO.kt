package com.example.domain.vo

data class MessageVO(
    val chatId : String? = "",

    val roomId: String? = "",

    val userId: String? = "",

    val messageBody: String? = "",

    val messageType: String? = "",
)

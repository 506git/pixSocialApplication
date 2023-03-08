package com.example.domain.vo

data class ChatListVO(

    val chat_id : String = "",

    val user_id: String = "",

    var message_profile : String= "",

    var message_name : String = "",

    val message_id : String = "",

    val message_body: String = "",

    val message_type: String = "",

    val createdAt: String = "",

    val message_sender : String = ""
)
package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class MessageDTO (
    @SerializedName("roomId")
    private val requestUserEmail: String? = "",

    @SerializedName("userId")
    private val userId: String? = "",

    @SerializedName("messageBody")
    private val messageBody: String? = "",

    @SerializedName("messageType")
    private val messageType: String? = "",

    private val createAt : String? = ""
)
package com.example.data.model

import com.google.gson.annotations.SerializedName

data class SendDTO(
    @SerializedName("title")
    private val title: String? = "",

    @SerializedName("message")
    private val message: String? = "",

    @SerializedName("deviceToken")
    private val deviceToken: String? = null
)
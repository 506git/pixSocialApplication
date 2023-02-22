package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class FriendsAddDTO(
    @SerializedName("requestUserEmail")
    private val requestUserEmail: String? = "",

    @SerializedName("receiveUserEmail")
    private val receiveUserEmail: String? = "",
)
package com.example.data.model

import com.google.gson.annotations.SerializedName

data class UserDTO (
    @SerializedName("userId")
    private val userId: String? = "",
)
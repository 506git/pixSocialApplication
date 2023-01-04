package com.example.data.model

import com.google.gson.annotations.SerializedName

data class PushRes (
    @SerializedName("resultCode")
    val resultCode : String? = "",

    @SerializedName("resultMessage")
    val resultMessage : String? = ""
)
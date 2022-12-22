package com.example.data.model

import com.google.gson.annotations.SerializedName

data class LawMaker (
    @SerializedName("name")
    val name: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("region")
    val political_party: String,
    val elected_time: String,
    val img_url: String
)
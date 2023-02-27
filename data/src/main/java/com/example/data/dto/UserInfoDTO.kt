package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class UserInfoDTO (
    @SerializedName("result")
    private val result: ResultInfo,
)

data class ResultInfo (
    @SerializedName("resultCode")
    private val resultCode: String = "",

    @SerializedName("resultMessage")
    private val resultMessage: String = "",

    @SerializedName("content")
    private val content: UserInfo,
)

data class UserInfo (
    @SerializedName("_id")
    private val _id: String = "",

    @SerializedName("user_id")
    private val user_id: String = "",

    @SerializedName("name")
    private val name: String,

    @SerializedName("email")
    private val email: String,

    @SerializedName("picture")
    private val picture: String? = null,

    @SerializedName("createdAt")
    private val createdAt: String? = null,

    @SerializedName("updatedAt")
    private val updatedAt: String? = null,

    @SerializedName("comment")
    private val comment: String? = null,
)
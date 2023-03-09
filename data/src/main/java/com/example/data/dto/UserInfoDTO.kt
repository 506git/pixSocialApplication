package com.example.data.dto

import com.google.gson.annotations.SerializedName

//data class UserInfoDTO (
//    @SerializedName("result")
//    private val result: ResultInfo,
//)
//
//data class ResultInfo (
//    @SerializedName("resultCode")
//    private val resultCode: String = "",
//
//    @SerializedName("resultMessage")
//    private val resultMessage: String = "",
//
//    @SerializedName("content")
//    private val content: UserInfo,
//)

data class UserInfoDTO (
    val _id: String = "",

    val user_id: String = "",

    val name: String,

    val email: String,

    val picture: String? = null,

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val comment: String? = null,
)
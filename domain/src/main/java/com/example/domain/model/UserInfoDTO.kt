package com.example.domain.model

//data class UserInfoDTO (
//    val result: ResultInfo,
//)
//
//data class ResultInfo (
//    val resultCode: String = "",
//
//    val resultMessage: String = "",
//
//    val content: UserInfo,
//)

data class UserInfoVO (
    val _id: String = "",

    val user_id: String = "",

    val name: String,

    val email: String,

    val picture: String? = null,

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val comment: String? = null,
)
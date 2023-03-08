package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val result: Result<T>,
)

data class Result<T> (
    val resultCode: String = "",

    val resultMessage: String = "",

    val content: T?
)
//
//
//data class ApiResponse<T>(
//    @SerializedName("result")
//    private val result: Result<T>,
//)
//
//data class Result<T> (
//    @SerializedName("resultCode")
//    private val resultCode: String = "",
//
//    @SerializedName("resultMessage")
//    private val resultMessage: String = "",
//
//    @SerializedName("content")
//    val data: T?
//)
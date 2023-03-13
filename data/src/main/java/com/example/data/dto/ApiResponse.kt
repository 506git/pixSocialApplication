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
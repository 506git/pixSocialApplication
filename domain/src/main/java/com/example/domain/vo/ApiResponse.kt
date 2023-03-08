package com.example.domain.vo

data class ApiResponse<T>(
    private val result: Result<T>,
)

data class Result<T> (
    private val resultCode: String = "",

    private val resultMessage: String = "",

    val data: T?
)
package com.example.data.dto

import com.example.domain.usecase.UpdateUserFcmToken

data class UserPushDTO (
    val userId : String? = "",
    val fcmToken: String? = "",
)
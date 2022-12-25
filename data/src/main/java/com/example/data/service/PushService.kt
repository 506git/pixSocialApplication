package com.example.data.service

import com.example.data.model.PushRes
import retrofit2.http.GET
import retrofit2.http.Query

interface PushService {
    @GET("push/send?")
    suspend fun sendPush(
        @Query("title") title: String = "",
        @Query("message") body: String = "",
        @Query("deviceToken") deviceToken: String = "",
    )
}

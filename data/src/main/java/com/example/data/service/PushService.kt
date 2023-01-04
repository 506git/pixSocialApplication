package com.example.data.service

import com.example.data.model.PushRes
import com.example.data.model.SendDTO
import retrofit2.http.*

interface PushService {
//    @GET("push/send-devicetoken?")
//    suspend fun sendPush(
//        @Query("title") title: String = "",
//        @Query("message") body: String = "",
//        @Query("deviceToken") deviceToken: String = "",
//    )

//    @GET("push/send-devicetoken?")
//    suspend fun sendPush(
//        @Query("title") title: String = "",
//        @Query("message") body: String = "",
//        @Query("deviceToken") deviceToken: String = "",
//    )


    @POST("push/send-devicetoken")
    suspend fun sendPush(
        @Body sendDTO: SendDTO
    )
}

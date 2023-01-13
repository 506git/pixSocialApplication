package com.example.data.service

import com.example.data.model.TestRes
import com.example.domain.model.Test
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface TestService {

    @GET("request/requestService.jsp?")
    suspend fun getTest(
        @Query("serviceName") serviceName: String = "MB_03_04_03_SERVICE",
        @Query("userId") userId: String = "imirinae",
        @Query("libCode") libCode : String = "111301",
        @Query("currentCount") page: Int = 1,
        @Query("pageCount") pageCount : Int = 10
    ) :  TestRes

    @GET("users/test")
    suspend fun getUser(
        @Header("authorization") accessToken: String
    )
}

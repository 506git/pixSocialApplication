package com.example.data.service

import com.example.data.model.LawMaker
import com.example.data.model.TestRes
import com.example.domain.model.Test
import retrofit2.http.GET
import retrofit2.http.Query

interface LawMakerService {

    @GET("requestService.jsp?")
    suspend fun getLawMaker(
        @Query("serviceName") serviceName: String = "MB_01_01_01_SERVICE"
    ) :  List<LawMaker>
}

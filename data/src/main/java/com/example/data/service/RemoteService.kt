package com.example.data.service

import com.example.data.dto.FriendsAddDTO
import com.example.data.dto.FriendsList
import com.example.data.dto.SendDTO
import com.example.data.model.TestRes
import com.example.data.model.UserDTO
import retrofit2.http.*

interface RemoteService {

    @GET("request/requestService.jsp?")
    suspend fun getTest(
        @Query("serviceName") serviceName: String = "MB_03_04_03_SERVICE",
        @Query("userId") userId: String = "imirinae",
        @Query("libCode") libCode : String = "111301",
        @Query("currentCount") page: Int = 1,
        @Query("pageCount") pageCount : Int = 10
    ) : TestRes

    @POST("users/google-login")
    suspend fun googleLogin(
        @Header("authorization") accessToken: String
    )

    @GET("users/one")
    suspend fun getUserInfo(
        @Header("authorization") accessToken: String
    )

    @POST("users/addFriend")
    suspend fun addFriend(
        @Body friendsAddDTO: FriendsAddDTO
    )

    @POST("users/myFriends")
    suspend fun getFriendsList(
        @Body userDTO : UserDTO // 수정
    ) : FriendsList
}

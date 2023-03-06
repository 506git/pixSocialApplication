package com.example.domain.repository

import com.example.domain.core.Result
import com.example.domain.model.*
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

interface AppDataRepository {
    suspend fun googleAutoLogIn() : Flow<Result<UserInfoDTO>>

    suspend fun signInWithCredential(token: String) : Flow<Result<UserInfoDTO>>
    suspend fun addFriends(friendsEmail: String) : Flow<Result<Unit>>

    suspend fun getUserInfo(id : String) : Flow<Result<User>>

    suspend fun getFriendsList(id : String) : Flow<Result<FriendsList>>

    suspend fun chatRoomStart(members : List<String>) : Flow<Result<RoomInfoDTO>>

    suspend fun getRoomInfo(id : String) : Flow<Result<RoomListInfoDTO>>

    suspend fun joinRoom(data : JSONObject) : Flow<Result<Unit>>
}
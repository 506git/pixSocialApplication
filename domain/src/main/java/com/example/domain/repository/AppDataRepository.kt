package com.example.domain.repository

import com.example.domain.core.Result
import com.example.domain.model.*
import com.example.domain.vo.ChatListVO
import com.example.domain.vo.MessageVO
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

interface AppDataRepository {
    suspend fun googleAutoLogIn() : Flow<Result<UserInfoVO>>

    suspend fun signInWithCredential(token: String) : Flow<Result<UserInfoVO>>
    suspend fun addFriends(friendsEmail: String) : Flow<Result<Unit>>

    suspend fun getUserInfo(id : String) : Flow<Result<User>>

    suspend fun updatePushToken(userId : String, token: String) : Flow<Result<Unit>>

    suspend fun getFriendsList(id : String) : Flow<Result<FriendsList>>

    suspend fun chatRoomStart(members : List<String>) : Flow<Result<RoomInfoDTO>>

    suspend fun getRoomInfo(id : String) : Flow<Result<RoomListInfoDTO>>

    suspend fun getChatList(roomId : String) : Flow<Result<List<ChatListVO>?>>

    suspend fun joinRoom(data : JSONObject) : Flow<Result<Unit>>

    suspend fun leaveRoom(data : JSONObject) : Flow<Result<Unit>>

    suspend fun sendMessage(data : JSONObject) : Flow<Result<Unit>>

    suspend fun receiveMessage() : Flow<Result<MessageVO>>
}
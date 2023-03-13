package com.example.domain.repository

import com.example.domain.core.Result
import com.example.domain.model.*
import com.example.domain.vo.ChatListVO
import com.example.domain.vo.FriendsInfoVO
import com.example.domain.vo.MessageVO
import com.example.domain.vo.RoomListInfoVO
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

interface AppDataRepository {
    suspend fun googleAutoLogIn() : Flow<Result<UserInfoVO>>

    suspend fun signInWithCredential(token: String) : Flow<Result<UserInfoVO>>
    suspend fun addFriends(friendsEmail: String) : Flow<Result<Unit>>

    suspend fun getUserInfo(id : String) : Flow<Result<User>>

    suspend fun updatePushToken(userId : String, token: String) : Flow<Result<Unit>>

    suspend fun getFriendsList(id : String) : Flow<Result<List<FriendsInfoVO>?>>

    suspend fun chatRoomStart(members : List<String>) :  Flow<Result<RoomListInfoVO>>

    suspend fun getRoomInfo(id : String) :Flow<Result<List<RoomListInfoVO>?>>

    suspend fun getChatList(roomId : String) : Flow<Result<List<ChatListVO>?>>

    suspend fun joinRoom(data : JSONObject) : Flow<Result<Unit>>

    suspend fun leaveRoom(data : JSONObject) : Flow<Result<Unit>>

    suspend fun sendMessage(data : JSONObject) : Flow<Result<Unit>>

    suspend fun receiveMessage() : Flow<Result<MessageVO>>

    fun uploadImage(message: String, userId : String) : Flow<Result<String>>
}
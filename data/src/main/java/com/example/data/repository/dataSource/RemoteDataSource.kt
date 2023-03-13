package com.example.data.repository.dataSource

import androidx.paging.PagingData
import com.example.data.dto.*
import com.example.data.model.UserDTO

import com.example.domain.model.LibraryDataSearchList
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
     fun getTestData(): Flow<PagingData<LibraryDataSearchList>>

     suspend fun googleLogin(accessToken : String) : ApiResponse<UserInfoDTO>

     suspend fun getUserInfo(uid : String)

     suspend fun updatePushToken(userId:String, token : String) : ApiResponse<Unit>

     suspend fun addFriendsAdd(friendsAddDTO: FriendsAddDTO)

     suspend fun getFriendsList(id : UserDTO) : ApiResponse<List<FriendInfoDTO>>

     suspend fun createRoom(createRoom : CreateRoomDTO) : ApiResponse<RoomListInfoDTO>

     suspend fun getRoomList(id : UserDTO) : ApiResponse<List<RoomListInfoDTO>>

     suspend fun getChatList(roomId : String) : ApiResponse<List<ChatListDTO>>
}
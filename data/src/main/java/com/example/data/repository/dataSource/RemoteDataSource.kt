package com.example.data.repository.dataSource

import androidx.paging.PagingData
import com.example.data.dto.*
import com.example.data.model.UserDTO

import com.example.domain.model.LibraryDataSearchList
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
     fun getTestData(): Flow<PagingData<LibraryDataSearchList>>

     suspend fun googleLogin(accessToken : String) : UserInfoDTO

     suspend fun getUserInfo(uid : String)

     suspend fun addFriendsAdd(friendsAddDTO: FriendsAddDTO)

     suspend fun getFriendsList(id : UserDTO) : FriendsList

     suspend fun createRoom(id : CreateRoomDTO) : RoomInfoDTO

     suspend fun getRoomList(id : UserDTO) : RoomListInfoDTO
}
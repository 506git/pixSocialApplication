package com.example.data.repository.dataSource

import androidx.paging.PagingData
import com.example.data.dto.FriendsAddDTO
import com.example.data.dto.FriendsList
import com.example.data.model.UserDTO

import com.example.domain.model.LibraryDataSearchList
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
     fun getTestData(): Flow<PagingData<LibraryDataSearchList>>

     suspend fun googleLogin(accessToken : String)

     suspend fun getUserInfo(uid : String)

     suspend fun addFriendsAdd(friendsAddDTO: FriendsAddDTO)

     suspend fun getFriendsList(id : UserDTO) : FriendsList
}
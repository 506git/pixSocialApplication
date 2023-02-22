package com.example.data.repository.dataSource

import androidx.paging.PagingData
import com.example.data.dto.FriendsAddDTO

import com.example.data.model.TestRes
import com.example.domain.model.LibraryDataSearchList
import com.example.domain.model.Test
import kotlinx.coroutines.flow.Flow

interface TestRemoteDataSource {
     fun getTestData(): Flow<PagingData<LibraryDataSearchList>>

     suspend fun googleLogin(accessToken : String)

     suspend fun getUserInfo(uid : String)

     suspend fun addFriendsAdd(friendsAddDTO: FriendsAddDTO)
}
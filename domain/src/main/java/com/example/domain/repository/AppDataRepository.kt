package com.example.domain.repository

import com.example.domain.core.Result
import com.example.domain.model.FriendsList
import com.example.domain.model.User
import com.example.domain.model.UserInfoDTO
import kotlinx.coroutines.flow.Flow

interface AppDataRepository {
    suspend fun googleAutoLogIn() : Flow<Result<UserInfoDTO>>

    suspend fun signInWithCredential(token: String) : Flow<Result<UserInfoDTO>>
    suspend fun addFriends(friendsEmail: String) : Flow<Result<Unit>>

    suspend fun getUserInfo(id : String) : Flow<Result<User>>

    suspend fun getFriendsList(id : String) : Flow<Result<FriendsList>>
}
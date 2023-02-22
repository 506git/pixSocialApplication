package com.example.domain.repository

import com.example.domain.core.Result
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AppDataRepository {
//    suspend fun getUserInfo(): Flow<Result<User>>
//    suspend fun insertUserInfo(): Flow<Result<Unit>>

    suspend fun addFriends(friendsEmail: String) : Flow<Result<Unit>>

    suspend fun getUserInfo(id : String) : Flow<Result<User>>
}
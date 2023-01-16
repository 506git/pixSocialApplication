package com.example.domain.repository

import com.example.domain.core.Result
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun getUserInfo(): Flow<Result<User>>
    suspend fun insertUserInfo(): Flow<Result<Unit>>
}
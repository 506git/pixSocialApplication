package com.example.domain.database_usecase

import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result
import com.example.domain.model.User
import com.example.domain.model.UserProfile
import com.example.domain.repository.DatabaseRepository

class InsertUserInfo(private val repository: DatabaseRepository) {
    suspend operator fun invoke(): Flow<Result<Unit>> {
        return repository.insertUserInfo()
    }
}
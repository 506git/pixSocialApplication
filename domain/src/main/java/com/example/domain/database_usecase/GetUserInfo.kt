package com.example.domain.database_usecase

import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result
import com.example.domain.model.User
import com.example.domain.repository.DatabaseRepository

class GetUserInfo(private val repository: DatabaseRepository) {
    suspend operator fun invoke(): Flow<Result<User>> {
        return repository.getUserInfo()
    }
}
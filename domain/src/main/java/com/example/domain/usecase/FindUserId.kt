package com.example.domain.usecase

import com.example.domain.model.UserProfile
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result

class FindUserId(private val repository: AppRepository) {
    suspend operator fun invoke(userId: String): Flow<Result<Unit>> {
        return repository.addUserId(userId)
    }
}
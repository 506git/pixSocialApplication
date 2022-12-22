package com.example.domain.usecase

import com.example.domain.model.UserProfile
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result

class InitUserProfileInfo(private val repository: AppRepository) {
    suspend operator fun invoke(): Flow<Result<Unit>> {
        return repository.initUserProfileInfo()
    }
}
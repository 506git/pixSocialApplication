package com.example.domain.usecase

import com.example.domain.model.UserProfile
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result

class UpdateUserProfile(private val repository: AppRepository) {
    suspend operator fun invoke(userName: String): Flow<Result<Unit>> {
        return repository.updateUserProfileToFirebaseDb(userName)
    }
}
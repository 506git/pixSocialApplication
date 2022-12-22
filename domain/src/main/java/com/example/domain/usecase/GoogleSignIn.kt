package com.example.domain.usecase

import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result

class GoogleSignIn(private val repository: AppRepository) {
    suspend operator fun invoke(): Flow<Result<Unit>> {
        return repository.googleSignIn()
    }
}